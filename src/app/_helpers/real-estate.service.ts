import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { observable, Observable } from 'rxjs';
import { observableToBeFn } from 'rxjs/internal/testing/TestScheduler';

@Injectable({
  providedIn: 'root'
})
export class RealEstateService {

  private countPlugin: any = 'http://localhost:9091/registry/getCounts';
  private getRegistryPlugin: any = 'http://localhost:9091/registry/getUserRegistrations';
  private getPropertyPlugin: any = 'http://localhost:9091/registry/getPropertyDetails';
  private isSaleAgreement: any = 'http://localhost:9091/registry/getValidSaleAgreement';
  private titleDeed: any = 'http://localhost:9091/registry/validateRegistration';
  private submitSaleAgrWorkflow: any = 'http://localhost:9091/registry/submitSaleAgreementWorkflow';
  private getUserWorkflows: any = 'http://localhost:9091/registry/getUserWorkflows';
  private getLoggedInUserDetails: any = 'http://localhost:9091/registry/user';
  private saveResponse: any = 'http://localhost:9091/registry/submitReponseWorkflow';
  private submitRegisWorkflow: any = 'http://localhost:9091/registry/submitRegistryWorkflow'
  private fetchPdf: any = 'http://localhost:9091/registry/generatepdf';

  queryParams: any;

  constructor(private _http: HttpClient) { }

  getUserDetails(userId: any) {
    return this._http.get<any>(this.getLoggedInUserDetails + '/' + userId);
  }

  getCount() {
    return this._http.get<any>(this.countPlugin);
  }

  getRegistryCount(userId: any) {
    return this._http.get<any>(this.getRegistryPlugin + '/' + userId);
  }

  getPropertyCount(propId: any) {
    return this._http.get<any>(this.getPropertyPlugin + '/' + propId);
  }

  checkSaleAgreementPresent(propId: any) {
    return this._http.get<any>(this.isSaleAgreement + '/' + propId);
  }

  checkSaleAgreementValidation(propId: any, aadhar: any) {
    return this._http.get<any>(this.titleDeed + '/' + propId + '/' + aadhar);
  }

  submitSaleAgreementWorkflow(tentativeDate: any, sellerUserFk: any, purchaserUserFk: any, propertyFk: any) {
    this.queryParams = { tentativeDate: tentativeDate, sellerUserFk: sellerUserFk, purchaserUserFk: purchaserUserFk, propertyFk: propertyFk }
    return this._http.post<any>(this.submitSaleAgrWorkflow, this.queryParams)
  }

  submitRegistryWorkflow(ownerFk: any, propertyFk: any, stampValue: string, buyType: any) {
    this.queryParams = { ownerFk: ownerFk, propertyFk: propertyFk, stampValue: stampValue, buyType: buyType }
    return this._http.post<any>(this.submitRegisWorkflow, this.queryParams)
  }

  getWorkflow(userId: any) {
    return this._http.get<any>(this.getUserWorkflows + '/' + userId);
  }

  saveResponseWorkflow(sellerComment: any, registrarComment: any, userRole: any, workFlowId: any, userId: any, response: any) {
    this.queryParams = { sellerComment: sellerComment, registrarComment: registrarComment, userRole: userRole, workFlowId: workFlowId, userId: userId, response: response };
    return this._http.post<any>(this.saveResponse, this.queryParams);
  }

  getPdf(id: any): Observable<Blob> {
    return this._http.get(`${this.fetchPdf}` + '/' + id, { responseType: 'blob' });
  }
}
