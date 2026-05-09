import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {Customer} from "../model/customer.model";
import {AccountsService} from "../services/accounts.service";
import {Observable} from "rxjs";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-customer-accounts',
  templateUrl: './customer-accounts.component.html',
  styleUrls: ['./customer-accounts.component.css']
})
export class CustomerAccountsComponent implements OnInit {
  customerId! : number ;
  customer! : Customer;
  accounts! : Observable<Array<any>>;
  newAccountFormGroup! : FormGroup;

  constructor(private route : ActivatedRoute, 
              private router :Router, 
              private accountService : AccountsService,
              private fb : FormBuilder,
              public authService : AuthService) {
    this.customer=this.router.getCurrentNavigation()?.extras.state as Customer;
  }

  ngOnInit(): void {
    this.customerId = this.route.snapshot.params['id'];
    this.handleGetAccounts();
    this.newAccountFormGroup = this.fb.group({
        accountType : this.fb.control('CURRENT'),
        initialBalance : this.fb.control(0, [Validators.required, Validators.min(0)]),
        overDraft : this.fb.control(0),
        interestRate : this.fb.control(0)
    });
  }

  handleGetAccounts() {
    this.accounts = this.accountService.getAccountsByCustomer(this.customerId);
  }

  handleSaveAccount() {
    let type = this.newAccountFormGroup.value.accountType;
    let balance = this.newAccountFormGroup.value.initialBalance;
    if(type == 'CURRENT') {
        this.accountService.saveCurrentAccount(balance, this.newAccountFormGroup.value.overDraft, this.customerId).subscribe({
            next : (data) => {
                alert("Current Account saved successfully!");
                this.handleGetAccounts();
            }
        });
    } else {
        this.accountService.saveSavingAccount(balance, this.newAccountFormGroup.value.interestRate, this.customerId).subscribe({
            next : (data) => {
                alert("Saving Account saved successfully!");
                this.handleGetAccounts();
            }
        });
    }
  }

}
