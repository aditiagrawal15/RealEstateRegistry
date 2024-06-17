import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, AbstractControl, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import { SubSink } from 'subsink';
import Swal from 'sweetalert2';
import { AuthService } from '../_helpers/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  aadhar!: string;
  password!: string;
  loggedInRole!: string;
  loginForm!: FormGroup;
  submitted = false;
  tmpindex: number = 0;
  showSetPassword: boolean = true;
  isLoading: boolean = false;

  loggedInChips: any = ['Buyer', 'Seller', 'Registrar'];

  constructor(private authService: AuthService, private router: Router, public sink: SubSink, private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      loginAs: ["BUYER", [
        Validators.required]],
      aadhar: ["", [
        Validators.required,
        Validators.pattern('^[0-9]{12}$')
      ]],
      password: ["", [Validators.required]],
    });

  }

  get f(): { [key: string]: AbstractControl } {
    return this.loginForm.controls;
  }

  onClickSubmit(data: any) {
    this.submitted = true;

    if (this.loginForm.invalid) {
      return;
    }
    this.aadhar = data.aadhar;
    this.password = data.password;
    this.loggedInRole = data.loginAs.toUpperCase();
    let isUserLoggedIn: boolean = false;
    let router = this.router;
    this.isLoading = true;
    this.sink.add(this.authService.logIn(this.aadhar, this.password, this.loggedInRole).subscribe(obj => {
      this.isLoading = false;
      Swal.fire({
        title: 'SUCCESS',
        icon: 'success',
        allowOutsideClick: false,
        allowEscapeKey: false,
        text: 'User is successfully logged in as ' + this.loginForm.value.loginAs,
        background: ' rgb(103, 152, 175)',
        confirmButtonColor: '#161d31'
      }).then(function () {
        isUserLoggedIn = true;
        localStorage.setItem('isUserLoggedIn', isUserLoggedIn ? "true" : "false");
        router.navigate(['dashboard'], {
          queryParams: { userId: obj.id, name: obj.nameWithRole, loginTime: obj.loginTime }
        });
      })

    },
      err => {
        this.isLoading = false;
        Swal.fire({
          title: 'ERROR',
          icon: 'error',
          allowOutsideClick: false,
          allowEscapeKey: false,
          text: err.error.errorMessage,
          background: ' rgb(103, 152, 175)',
          confirmButtonColor: '#161d31'
        })

      }));

  }
  setPassword() {
    this.router.navigate(['set-password']);
  }

  selectedChip(chip: any) {
    if (chip === 'Registrar')
      this.showSetPassword = false;
    else
      this.showSetPassword = true;
    this.loginForm.controls?.['loginAs'].setValue(chip)
  }

  ngOnDestroy(): void {
    this.sink.unsubscribe();
  }

}

