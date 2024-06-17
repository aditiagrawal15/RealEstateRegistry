import { trigger, state, style, transition, animate } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Route, Router } from '@angular/router';
import { RealEstateService } from '../_helpers/real-estate.service';
import Swal from 'sweetalert2';
import { SubSink } from 'subsink';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
  animations: [
    trigger('flipState', [
      state('active', style({
        transform: 'rotateY(179deg)'
      })),
      state('inactive', style({
        transform: 'rotateY(0)'
      })),
      transition('active => inactive', animate('500ms ease-out')),
      transition('inactive => active', animate('500ms ease-in'))
    ])
  ]

})
export class DashboardComponent implements OnInit {

  constructor(public router: Router, private sink: SubSink, private activatedRoute: ActivatedRoute, private realEstate: RealEstateService) { }

  loggedInUserName: any;
  userId!: string;
  loggedInTime!: any;
  name!:any;
  totalReg!: any;
  totalUsers!: any;
  portalUsb!: any;
  totalOwnedPro!: any;
  isLoading: boolean = false;

  ngOnInit(): void {
    this.isLoading = true;
    this.sink.add(this.activatedRoute.queryParamMap.subscribe(params => {
      this.userId = params.getAll('userId')[0];
      this.loggedInUserName = params.getAll('name')[0];
      this.name = this.loggedInUserName.split('(')[0];
      this.loggedInTime = params.getAll('loginTime')[0];
    }));
    this.sink.add(this.realEstate.getCount().subscribe(data => {
      this.isLoading = false;
      this.totalReg = data?.registryNagarpalikaCount;
      this.totalUsers = data?.registryUserCount;
      this.portalUsb = data?.loggedInUserCount;
      this.totalOwnedPro = data?.propertyCount;
    },
      err => {
        this.isLoading = false;
        Swal.fire({
          title: 'ERROR',
          icon: 'error',
          text: err.error.errorMessage,
          background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
          confirmButtonColor: 'rgb(185, 122, 87)',
          allowOutsideClick: false,
          allowEscapeKey: false
        })
      }));
  }
  flip: string = 'inactive';

  toggleFlip() {
    this.flip = (this.flip == 'inactive') ? 'active' : 'inactive';
  }
  ngOnDestroy(): void {
    this.sink.unsubscribe();
  }


}
