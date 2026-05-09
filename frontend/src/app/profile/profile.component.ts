import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../services/auth.service";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  passwordFormGroup! : FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService) { }

  ngOnInit(): void {
    this.passwordFormGroup = this.fb.group({
      oldPassword: this.fb.control('', [Validators.required]),
      newPassword: this.fb.control('', [Validators.required]),
      confirmPassword: this.fb.control('', [Validators.required])
    });
  }

  handleChangePassword() {
    let oldPassword = this.passwordFormGroup.value.oldPassword;
    let newPassword = this.passwordFormGroup.value.newPassword;
    let confirmPassword = this.passwordFormGroup.value.confirmPassword;
    this.authService.changePassword(oldPassword, newPassword, confirmPassword).subscribe({
      next: (data) => {
        alert("Password updated successfully");
        this.passwordFormGroup.reset();
      },
      error: (err) => {
        alert(err.message);
      }
    });
  }
}
