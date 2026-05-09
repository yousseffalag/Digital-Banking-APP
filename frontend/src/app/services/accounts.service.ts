import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {AccountDetails} from "../model/account.model";

@Injectable({
  providedIn: 'root'
})
export class AccountsService {

  constructor(private http : HttpClient) { }

  public getAccount(accountId : string, page : number, size : number):Observable<AccountDetails>{
    return this.http.get<AccountDetails>(environment.backendHost+"/accounts/"+accountId+"/pageOperations?page="+page+"&size="+size);
  }
  public debit(accountId : string, amount : number, description:string){
    let data={accountId : accountId, amount : amount, description : description}
    return this.http.post(environment.backendHost+"/accounts/debit",data);
  }
  public credit(accountId : string, amount : number, description:string){
    let data={accountId : accountId, amount : amount, description : description}
    return this.http.post(environment.backendHost+"/accounts/credit",data);
  }
  public transfer(accountSource: string,accountDestination: string, amount : number, description:string){
    let data={accountSource, accountDestination, amount, description }
    return this.http.post(environment.backendHost+"/accounts/transfer",data);
  }
  public getAccountsByCustomer(customerId : number):Observable<any>{
    return this.http.get(environment.backendHost+"/customers/"+customerId+"/accounts");
  }
  public saveCurrentAccount(initialBalance : number, overDraft : number, customerId : number){
    let data={initialBalance, overDraft, customerId}
    return this.http.post(environment.backendHost+"/accounts/current",data);
  }
  public saveSavingAccount(initialBalance : number, interestRate : number, customerId : number){
    let data={initialBalance, interestRate, customerId}
    return this.http.post(environment.backendHost+"/accounts/saving",data);
  }
  public updateStatus(accountId : string, status : string){
    return this.http.put(environment.backendHost+"/accounts/"+accountId+"/status?status="+status, {});
  }
}
