import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, AbstractControl, Validators, FormBuilder } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { AuthService } from '../_helpers/auth.service';

@Component({
  selector: 'app-set-password',
  templateUrl: './set-password.component.html',
  styleUrls: ['./set-password.component.css']
})
export class SetPasswordComponent implements OnInit {

  setPasswordForm!: FormGroup;
  submitted = false;

  isLoading: boolean = false;

  constructor(private authService: AuthService, private router: Router, private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    this.setPasswordForm = this.formBuilder.group({
      aadhar: ["", [
        Validators.required,
        Validators.pattern('^[0-9]{12}$')
      ]],
      password: ["", [Validators.required, Validators.minLength(6), Validators.maxLength(12)]],
      // confirmPassword: ['', Validators.required],
    })
    // {
    //   validators: [Validation.match('password', 'confirmPassword')]
    // });
  }

  get f(): { [key: string]: AbstractControl } {
    return this.setPasswordForm.controls;
  }

  cancel() {
    this.router.navigate(['login']);
  }

  onClickSubmit(data: any) {
    this.submitted = true;

    if (this.setPasswordForm.invalid) {
      return;
    }
    let aadhar = data.aadhar;
    let password = data.password;
    let router = this.router;
    this.isLoading = true;
    this.authService.setPassword(aadhar, password).subscribe(data => {
      this.isLoading = false;
      Swal.fire({
        title: 'SUCCESS',
        icon: 'success',
        allowOutsideClick: false,
        allowEscapeKey: false,
        text: 'Password is successfully set for ' + aadhar + '. Please login again.',
        background: ' rgb(103, 152, 175)',
        confirmButtonColor: '#161d31'
      }).then(function () {
        router.navigate(['login']);
      });
    }

      ,
      err => {
        this.isLoading = false;
        Swal.fire({
          title: 'ERROR',
          icon: 'error',
          allowOutsideClick: false,
          allowEscapeKey: false,
          text: err.error.errorMessage + ' ' + err.error.recoveryMessage,
          background: ' rgb(103, 152, 175)',
          confirmButtonColor: '#161d31'
        })

      })

  }

}
