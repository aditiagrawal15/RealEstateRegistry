import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { SubSink } from 'subsink';

@Component({
  selector: 'app-buy-property',
  templateUrl: './buy-property.component.html',
  styleUrls: ['./buy-property.component.css']
})
export class BuyPropertyComponent implements OnInit {

  loggedInUserName: any;
  userId!: string;
  loggedInTime!: any;

  constructor(public router: Router, private sink: SubSink, private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.sink.add(this.activatedRoute.queryParamMap.subscribe(params => {
      this.userId = params.getAll('userId')[0];
      this.loggedInUserName = params.getAll('name')[0];
      this.loggedInTime = params.getAll('loginTime')[0];
    }));
  }

}
