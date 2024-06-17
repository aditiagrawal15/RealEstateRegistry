import { Time } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import Swal from 'sweetalert2';
import { AuthService } from '../_helpers/auth.service';
import { RealEstateService } from '../_helpers/real-estate.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  @Input() userId!: string;
  @Input() loggedInTime!: Time;
  @Input() loggedInUserName!: string;
  notificationCount: number = 0;
  hideTabRegistrar: boolean = false;
  hideTabSeller: boolean = false;
  previousUrl!: string;
  currentUrl!: string;

  constructor(private authService: AuthService, public router: Router, private realEstate: RealEstateService) {

  }

  ngOnInit(): void {
    this.loadDataForNotificationCount();
    if (this.loggedInUserName.includes('REGISTRAR'))
      this.hideTabRegistrar = true;
    else if (this.loggedInUserName.includes('SELLER'))
      this.hideTabSeller = true;
  }

  loadDataForNotificationCount() {
    this.realEstate.getWorkflow(this.userId)
      .subscribe(data => {
        this.notificationCount = data.length;

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

  logoutFromApp() {
    this.authService.getLogOut(this.userId).subscribe(data => {
      this.authService.getLogOut(this.userId);
      localStorage.removeItem('isUserLoggedIn');
      this.router.navigate(['/']);
    },
      err => {
        Swal.fire({
          title: 'ERROR',
          icon: 'error',
          allowOutsideClick: false,
          allowEscapeKey: false,
          text: err.error.errorMessage + err.error.recoveryMessage,
          background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
          confirmButtonColor: 'rgb(185, 122, 87)',
        })

      })


  }

  buyProperty() {
    this.router.navigate(['buy-property'], {
      queryParams: { userId: this.userId, name: this.loggedInUserName, loginTime: this.loggedInTime }
    });
    Swal.fire({
      title: 'ARE YOU SURE?',
      icon: "warning",
      text: "You will be redirected to a third party app, do you wish to continue?",
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
        window.open('https://ibapi.in/', '_blank');
      }
      else {
        // this.router.events.pipe(
        //   filter((event) => event instanceof NavigationEnd)
        // ).subscribe((event:any) => {
        //   this.previousUrl = this.currentUrl;
        //   this.currentUrl = event.url;
        // });
      }
    })
  }

  showRegistrationWarning() {
    if (this.router.url.includes('new-registration')) {

    }
    else {
      Swal.fire({
        title: 'WARNING',
        icon: "warning",
        text: "Successful Registration of a property updates property ownership in Sub Registrar's Office and Nagar Palika Records. Please press Yes if, you wish to proceed.",
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
          this.router.navigate(['new-registration'], {
            queryParams: { userId: this.userId, name: this.loggedInUserName, loginTime: this.loggedInTime }
          });
        }
      })
    }

  }

}
