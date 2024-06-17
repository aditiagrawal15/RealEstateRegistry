import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { RealEstateService } from 'src/app/_helpers/real-estate.service';
import { SubSink } from 'subsink';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-registry-view-details',
  templateUrl: './registry-view-details.component.html',
  styleUrls: ['./registry-view-details.component.css']
})
export class RegistryViewDetailsComponent implements OnInit {

  loggedInUserName: any;
  userId!: string;
  loggedInTime!: any;
  cards: any = [];
  data: any;


  isLoading: boolean = false;

  constructor(public router: Router, private sink: SubSink, private activatedRoute: ActivatedRoute, private realEstate: RealEstateService) { }

  ngOnInit(): void {
    this.isLoading = true;
    this.sink.add(this.activatedRoute.queryParamMap.subscribe(params => {
      this.userId = params.getAll('userId')[0];
      this.loggedInUserName = params.getAll('name')[0];
      this.loggedInTime = params.getAll('loginTime')[0];
    }));
    this.sink.add(this.realEstate.getRegistryCount(this.userId).subscribe(data => {
      this.isLoading = false;
      this.data = data;
      this.cards = [{
        title: 'Registry Number: ' + this.data[0].regNumber,
        subtitle: 'Type of Buying: ' + this.data[0].buyType,
        propId: 'Property Id: ' + this.data[0].propertyFk['propertyId'],
        propLoc: 'Property Location: ' + this.data[0].propertyFk['propertyLocation'] + ', ' + this.data[0].propertyFk['propertyPincode'],
        currentOwn: 'Current Owner: ' + this.data[0].ownerFk['name'] + '( ' + this.data[0].ownerFk['aadharNumber'] + ' )',
      }]
    },
      err => {
        this.isLoading = false;
        Swal.fire({
          title: 'ERROR',
          icon: 'error',
          allowOutsideClick: false,
          allowEscapeKey: false,
          text: err.error.errorMessage,
          background: 'linear-gradient(rgb(185, 122, 87),#EFE4B0)',
          confirmButtonColor: 'rgb(185, 122, 87)',
        })
      }));
  }
  ngOnDestroy(): void {
    this.sink.unsubscribe();
  }
}
