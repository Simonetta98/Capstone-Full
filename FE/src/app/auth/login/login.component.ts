import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, NgForm, Validators} from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm!:FormGroup;

  loginF!:FormGroup;

  constructor(private as:AuthService, private router:Router) { }

  ngOnInit(): void {
  this.loginForm= new FormGroup({
      username: new FormControl(null, Validators.required),
      password: new FormControl(null, Validators.required)
  })

  }

  login(){

      let value={
          username: this.loginForm.controls["username"].value,
          password: this.loginForm.controls['password'].value
      }

      console.log(value);

    this.as.login(value).subscribe((res)=>{
      this.router.navigate(['/dashboard'])
    })
  }

}
