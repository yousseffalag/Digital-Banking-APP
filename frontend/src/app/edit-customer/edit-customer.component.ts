import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {CustomerService} from "../services/customer.service";
import {Customer} from "../model/customer.model";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-edit-customer',
  templateUrl: './edit-customer.component.html',
  styleUrls: ['./edit-customer.component.css']
})
export class EditCustomerComponent implements OnInit {
  customerId! : number;
  customer! : Customer;
  editCustomerFormGroup! : FormGroup;

  constructor(private route : ActivatedRoute, 
              private customerService : CustomerService, 
              private fb : FormBuilder,
              private router : Router) { 
    this.customerId = this.route.snapshot.params['id'];
  }

  ngOnInit(): void {
    this.customerService.getCustomer(this.customerId).subscribe({
      next : (data) => {
        this.customer = data;
        this.editCustomerFormGroup = this.fb.group({
          name : this.fb.control(this.customer.name, [Validators.required, Validators.minLength(4)]),
          email : this.fb.control(this.customer.email, [Validators.required, Validators.email])
        });
      },
      error : (err) => {
        console.log(err);
      }
    });
  }

  handleUpdateCustomer() {
    let customer : Customer = this.editCustomerFormGroup.value;
    customer.id = this.customerId;
    this.customerService.updateCustomer(customer).subscribe({
      next : (data) => {
        alert("Customer updated successfully!");
        this.router.navigateByUrl("/admin/customers");
      },
      error : (err) => {
        console.log(err);
      }
    });
  }
}
