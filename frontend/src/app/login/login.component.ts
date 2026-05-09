import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginFormGroup! : FormGroup
  constructor(private fb : FormBuilder, private authService : AuthService, private router : Router) { }

  ngOnInit(): void {
    this.loginFormGroup = this.fb.group({
      username : this.fb.control(""),
      password : this.fb.control("")
    })
  }

  handleLogin(){
    this.authService.login(this.loginFormGroup.value.username, this.loginFormGroup.value.password)
    .subscribe({
      next : (data) => {
        this.authService.loadProfile(data);
        this.router.navigateByUrl("/admin/customers");

      },
      error : (err) => {
        console.log(err);
      }
    });
    console.log(this.loginFormGroup.value);
  }

}
