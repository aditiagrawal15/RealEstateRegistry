import { Component, OnInit, ViewChild } from '@angular/core';
import { Validators, FormBuilder, FormGroup, FormControl, AbstractControl } from '@angular/forms';
import { MatStepper } from '@angular/material/stepper';
import { ActivatedRoute, Router } from '@angular/router';
import { RealEstateService } from 'src/app/_helpers/real-estate.service';
import { SubSink } from 'subsink';
import Swal from 'sweetalert2';
import { propertyFk, purchaserUserFk, sellerUserFk } from './SaleAgreementModal';

@Component({
  selector: 'app-new-registration',
  templateUrl: './new-registration.component.html',
  styleUrls: ['./new-registration.component.css']
})
export class NewRegistrationComponent implements OnInit {

  loggedInUserName: any;
  userId!: string;
  loggedInTime!: any;
  name!: string;

  isLinear = true;
  directBuying: boolean = false;
  timeRequired: boolean = false;

  typeOfProperty: any = ['House', 'Shop', 'Land'];
  typeOfBuying: any = ['Direct', 'Time Required'];

  firstFormGroup!: FormGroup;
  secondFormGroup!: FormGroup;
  step1Completed: boolean = false;

  isLoading: boolean = false;

  private tentativeDate!: any;
  private sellerUserFk!: sellerUserFk;
  private purchaserUserFk!: purchaserUserFk;
  private propertyFk!: propertyFk;

  showCalculation: boolean = false;
  propertyValue: number = 0;
  propertyValueWithComma: number = 0;
  stampValue: number = 0;
  gst: number = 0;
  finalPayment: number = 0;
  propertyId: number = 0;
  showThankYou: boolean = false;

  disableSubmit: boolean = true;
  disablePrevious: boolean = false;

  submitted = false;

  @ViewChild('stepper') stepper!: MatStepper;

  constructor(private formBuilder: FormBuilder, public router: Router, private sink: SubSink, private activatedRoute: ActivatedRoute, private realEstate: RealEstateService) { }

  ngOnInit(): void {

    this.sink.add(this.activatedRoute.queryParamMap.subscribe(params => {
      this.userId = params.getAll('userId')[0];
      this.loggedInUserName = params.getAll('name')[0];
      this.name = this.loggedInUserName.split('(')[0];
      this.loggedInTime = params.getAll('loginTime')[0];
    }));
    this.firstFormGroup = this.formBuilder.group({
      typeOfProperty: new FormControl(""),
      typeOfBuying: new FormControl("", [Validators.required]),
      propertyId: new FormControl("", [Validators.required, Validators.pattern("^[0-9]*$")]),
      agreementId: new FormControl("", [ Validators.pattern("^[0-9]*$")]),
      sellerAadhar: new FormControl("", [Validators.required,
      Validators.pattern('^[0-9]{12}$')])
    });
    this.secondFormGroup = this.formBuilder.group({
      purchaser: new FormControl(this.name, [Validators.required]),
      sellerAadhar: new FormControl("", [Validators.required]),
      tentativeDate: new FormControl("", [Validators.required]),
      stampValue: new FormControl("", [Validators.required])
    })
  }
  get f(): { [key: string]: AbstractControl } {
    return this.firstFormGroup.controls;
  }


  next(data: any) {
    this.submitted = true;
     if (this.firstFormGroup.invalid) {
      return;
    }
    this.isLoading = true;
    const propId = data.propertyId;
    const sellerAadhar = data.sellerAadhar;
    const typeOfBuying = data.typeOfBuying;
    this.secondFormGroup.controls?.['sellerAadhar'].setValue(sellerAadhar);
    if (typeOfBuying === 'Direct') {
      this.directBuying = true;
      this.timeRequired = false;
    }
    else {
      this.timeRequired = true;
      this.directBuying = false;
    }
    this.sink.add(this.realEstate.checkSaleAgreementValidation(propId, sellerAadhar).subscribe(data => {
      this.step1Completed = true;
      this.isLoading = false;
      this.stepper.next();
    },
      err => {
        this.isLoading = false;
        Swal.fire({
          title: 'ERROR',
          icon: 'error',
          allowOutsideClick: false,
          allowEscapeKey: false,
          text: err.error.errorMessage + err.error.recoveryMessage,
          confirmButtonText: 'Yes',
          background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
          confirmButtonColor: 'rgb(185, 122, 87)',
          customClass: {
            actions: 'my-actions',
            cancelButton: 'order-1',
            confirmButton: 'order-2',
          }
        })
      }))
  }

  payNow() {
    Swal.fire({
      title: 'ARE YOU SURE?',
      icon: "warning",
      text: "Are you sure you want to make a payment of INR " + this.finalPayment + '?',
      showCancelButton: true,
      allowOutsideClick: false,
      allowEscapeKey: false,
      confirmButtonText: 'Yes',
      background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
      confirmButtonColor: 'rgb(185, 122, 87)',
      customClass: {
        actions: 'my-actions',
        cancelButton: 'order-1',
        confirmButton: 'order-2',
      }
    }).then((result) => {
      if (result.isConfirmed) {
        Swal.fire({
          title: 'SUCCESS',
          icon: 'success',
          allowOutsideClick: false,
          allowEscapeKey: false,
          text: 'Thank you for making a payment of Rs. ' + this.finalPayment + ' to us. Please click submit to complete your registration process of property with Id ' + this.propertyId + '.',
          background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
          confirmButtonColor: 'rgb(185, 122, 87)',
        }).then(() => {
          this.showThankYou = true;
          this.showCalculation = false;
          this.disableSubmit = false;
          this.disablePrevious = true;

        })
      }
      else {

      }
    })
  }

  calculate() {
    this.isLoading = true;
    this.realEstate.getPropertyCount(this.firstFormGroup.value.propertyId).subscribe(
      data => {
        this.isLoading = false;
        this.propertyId = data.propertyId
        this.propertyValueWithComma = data.propertyValueWithComma;
        this.propertyValue = data.propertyValue;
        this.stampValue = this.stampValue + (7 * this.propertyValue) / 100 + (1 * this.propertyValue) / 100;
        this.gst = this.gst + (18 * this.stampValue) / 100;
        this.finalPayment = this.stampValue + this.gst;
        this.showCalculation = true;
        this.secondFormGroup.controls?.['stampValue'].setValue(this.stampValue)

      },
      err => {
        this.isLoading = false;
      }
    )
  }

  saveSaleAgreement() {
    this.isLoading = true;
    this.tentativeDate = this.secondFormGroup.value.tentativeDate;
    this.sellerUserFk = new sellerUserFk();
    this.sellerUserFk.aadharNumber = Number(this.secondFormGroup.value.sellerAadhar);
    this.purchaserUserFk = new purchaserUserFk();
    this.purchaserUserFk.id = Number(this.userId);
    this.propertyFk = new propertyFk();
    this.propertyFk.propertyId = Number(this.firstFormGroup.value.propertyId);
    this.realEstate.submitSaleAgreementWorkflow(this.tentativeDate, this.sellerUserFk, this.purchaserUserFk, this.propertyFk).subscribe(data => {
      this.isLoading = false;
      Swal.fire({
        title: 'SUCCESS',
        icon: 'success',
        allowOutsideClick: false,
        allowEscapeKey: false,
        text: 'A request is raised with Id ' + data.id + ' for creating Sale Agreement. Please check My Workflow for detailed information.',
        background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
        confirmButtonColor: 'rgb(185, 122, 87)',
      }).then(() => {
        window.location.reload();

      })
    },
      err => {
        this.isLoading = false;
        Swal.fire({
          title: 'ERROR',
          icon: 'error',
          allowOutsideClick: false,
          allowEscapeKey: false,
          text: err.error.errorMessage ? err.error.errorMessage : err.error.error,
          background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
          confirmButtonColor: 'rgb(185, 122, 87)',
        })
      })
  }

  saveRegistry() {
    this.isLoading = true;
    let stampValue = String(this.secondFormGroup.value.stampValue);
    let buyType = this.firstFormGroup.value.typeOfBuying;
    this.propertyFk = new propertyFk();
    this.propertyFk.propertyId = Number(this.firstFormGroup.value.propertyId);
    let owner_Fk = new purchaserUserFk();
    owner_Fk.id = Number(this.userId);
    this.realEstate.submitRegistryWorkflow(owner_Fk, this.propertyFk, stampValue, buyType).subscribe(data => {
      this.isLoading = false;
      Swal.fire({
        title: 'SUCCESS',
        icon: 'success',
        allowOutsideClick: false,
        allowEscapeKey: false,
        text: 'A request is raised with Id ' + data.id + ' for Registering property ' + this.firstFormGroup.value.propertyId + '. Please check My Workflow for detailed information.',
        background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
        confirmButtonColor: 'rgb(185, 122, 87)',
      }).then(() => {
        window.location.reload();

      })
    },
      err => {
        this.isLoading = false;
        Swal.fire({
          title: 'ERROR',
          icon: 'error',
          allowOutsideClick: false,
          allowEscapeKey: false,
          text: err.error.errorMessage ? err.error.errorMessage : err.error.error,
          background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
          confirmButtonColor: 'rgb(185, 122, 87)',
        })
      })
  }

  cancel() {

  }


}
