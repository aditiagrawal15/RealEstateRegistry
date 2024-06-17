import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { RealEstateService } from 'src/app/_helpers/real-estate.service';
import { SubSink } from 'subsink';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-property-view-details',
  templateUrl: './property-view-details.component.html',
  styleUrls: ['./property-view-details.component.css']
})
export class PropertyViewDetailsComponent implements OnInit {
  loggedInUserName: any;
  userId!: string;
  loggedInTime!: any;
  cards: any = [];
  data: any;
  getPropertyForm!: FormGroup;
  propertyId!: any;
  submitted = false;

  isLoading: boolean = false;

  constructor(public router: Router, private sink: SubSink, private formBuilder: FormBuilder, private activatedRoute: ActivatedRoute, private realEstate: RealEstateService) { }

  ngOnInit(): void {
    this.getPropertyForm = this.formBuilder.group({
      propertyId: new FormControl("", [Validators.required, Validators.pattern("^[0-9]*$")])
    });
    this.sink.add(this.activatedRoute.queryParamMap.subscribe(params => {
      this.userId = params.getAll('userId')[0];
      this.loggedInUserName = params.getAll('name')[0];
      this.loggedInTime = params.getAll('loginTime')[0];
    }));
  }

  get f(): { [key: string]: AbstractControl } {
    return this.getPropertyForm.controls;
  }

  onClickSubmit(data: any) {
    this.submitted = true;

    this.isLoading = true;
    if (this.getPropertyForm.invalid) {
      this.isLoading = false;
      return;
    }
    this.propertyId = data.propertyId;
    this.sink.add(this.realEstate.getPropertyCount(this.propertyId).subscribe(obj => {
      this.isLoading = false;
      this.data = obj;
      this.cards = [{
        title: 'Property Id: ' + this.data.propertyId,
        subtitle: 'Property Location: ' + this.data.propertyLocation + ', ' + this.data.propertyPincode,
        propArea: 'Property Area: ' + this.data.propertyArea + ' sq ft',
        propType: 'Property Type: ' + this.data.propertyType,
        propPlotNo: 'Plot No: ' + this.data.plotNo,
        propDistrict: 'District: ' + this.data.district,
        propValue: 'Property Value: ' + this.data.propertyValueWithComma,
        currentOwn: 'Current Owner: ' + this.data.currentOwnerFk['name'] + '( ' + this.data.currentOwnerFk['aadharNumber'] + ' )',
      }]
    },
      err => {
        this.isLoading = false;
        this.cards = [];
        Swal.fire({
          title: 'ERROR',
          icon: 'error',
          allowOutsideClick: false,
          allowEscapeKey: false,
          text: err.error.errorMessage ? err.error.errorMessage : err.error.error,
          background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
          confirmButtonColor: 'rgb(185, 122, 87)',
        })

      }));

  }
  ngOnDestroy(): void {
    this.sink.unsubscribe();
  }
}
