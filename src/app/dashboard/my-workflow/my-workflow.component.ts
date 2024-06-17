import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RealEstateService } from 'src/app/_helpers/real-estate.service';
import { SubSink } from 'subsink';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import Swal from 'sweetalert2';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs';

@Component({
  selector: 'app-my-workflow',
  templateUrl: './my-workflow.component.html',
  styleUrls: ['./my-workflow.component.css']
})
export class MyWorkflowComponent implements OnInit {

  loggedInUserName: any;
  userId!: string;
  loggedInTime!: any;

  workFlowData: any = [];
  role!: string;
  registrarAssigned!: string;
  userAadhar!: any;
  responseForm!: FormGroup;
  submitted = false;

  constructor(private _http: HttpClient, public router: Router, private sink: SubSink, private formBuilder: FormBuilder, private activatedRoute: ActivatedRoute, private realEstate: RealEstateService) { }

  ngOnInit(): void {
    this.responseForm = this.formBuilder.group({
      comments: new FormControl("", [Validators.required])
    });
    this.sink.add(this.activatedRoute.queryParamMap.subscribe(params => {
      this.userId = params.getAll('userId')[0];
      this.loggedInUserName = params.getAll('name')[0];
      this.loggedInTime = params.getAll('loginTime')[0];
    }));
    this.getLoggedInUserRole();
    this.loadData();
  }

  getLoggedInUserRole() {
    this.realEstate.getUserDetails(this.userId).subscribe(data => {
      this.role = data.loggedInRole;
      this.userAadhar = data.aadharNumber;
    },
      err => {
        Swal.fire({
          title: 'ERROR',
          icon: 'error',
          text: err.error.errorMessage ? err.error.errorMessage : err.error.error,
          background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
          confirmButtonColor: 'rgb(185, 122, 87)',
        })
      })
  }
  loadData() {
    this.realEstate.getWorkflow(this.userId)
      .subscribe(data => {
        this.workFlowData = data;

      }, err => {
        Swal.fire({
          title: 'ERROR',
          icon: 'error',
          text: err.error.errorMessage ? err.error.errorMessage : err.error.error,
          background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
          confirmButtonColor: 'rgb(185, 122, 87)',
        })
      });
  }

  saveResponse(status: any, workflowId: any, data: any) {
    this.submitted = true;

    if (this.responseForm.invalid) {
      return;
    }
    let sellerComment: any = '';
    let registrarComment: any = '';
    if (this.role === 'SELLER')
      sellerComment = data.comments;
    else if (this.role === 'REGISTRAR')
      registrarComment = data.comments;

    let userRole = this.role;
    let workFlowId = workflowId;
    let userId = this.userId;
    let response = status;

    this.realEstate.saveResponseWorkflow(sellerComment, registrarComment, userRole, workFlowId, userId, response).subscribe(data => {
      Swal.fire({
        title: 'SUCCESS',
        icon: 'success',
        allowOutsideClick: false,
        allowEscapeKey: false,
        text: 'Thank you for your response. We have updated the concerned person.',
        background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
        confirmButtonColor: 'rgb(185, 122, 87)',
      }).then(function () {
        window.location.reload();
      })
    },
      err => {
        Swal.fire({
          title: 'ERROR',
          icon: 'error',
          text: err.error.errorMessage ? err.error.errorMessage : err.error.error,
          background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
          confirmButtonColor: 'rgb(185, 122, 87)',
        })
      })
  }

  get f(): { [key: string]: AbstractControl } {
    return this.responseForm.controls;
  }


  downloadPdf(workFlowId: any, pdfName: string) {
    this.realEstate.getPdf(workFlowId).subscribe(res => {
      const blob = new Blob([res], {
        type: 'application/pdf'
      });
      const data = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = data;
      link.download = pdfName + '.pdf';
      link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }))
      setTimeout(function () {
        window.URL.revokeObjectURL(data);
        link.remove();
      }, 100)
    },
      err => {
        Swal.fire({
          title: 'ERROR',
          icon: 'error',
          text: err.error.errorMessage ? err.error.errorMessage + err.error.recoveryMessage : err.error.error,
          background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
          confirmButtonColor: 'rgb(185, 122, 87)',
        })
      })
  }

}
