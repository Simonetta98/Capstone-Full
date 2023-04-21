import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/models/models';
import { AuthService } from 'src/app/services/auth.service';
import { DetailsService } from 'src/app/services/details.service';
import { ProfileService } from 'src/app/services/profile.service';
import { UserService } from 'src/app/services/user.service';

@Component({
    selector: 'app-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {

    isLoggedIn: boolean = false;

    searchText?: string;
    users: User[] = [];
    usernames: String[] = [];
    filteredUsernames: any[] = [];

    constructor(private authSrv: AuthService, private http: HttpClient, private profileSrv: ProfileService, private userSrv: UserService, private detailsSrv: DetailsService) { }

    ngOnInit(): void {
        this.authSrv.isLoggedIn$.subscribe((isLoggedIn) => {
            this.isLoggedIn = isLoggedIn;
        });

        this.getAllUsers();
    }

    onLogout() {
        this.authSrv.logout();
    }

    public getAllUsers(){
        this.userSrv.getAllUsers().subscribe(res=>{
            //console.log(res)
            this.users = res.content as User[];
            //console.log(this.users)
            this.users.forEach(u => {
                this.usernames.push(u.username)
                //console.log(this.usernames)
            });
        })
    };

    public filter(event: any){
    this.searchText = event.toString().toLowerCase();
    if(this.searchText){
        this.filteredUsernames = this.usernames.filter((element) => {
            return element.toLowerCase() == this.searchText;
        });
    }
  };

//SEND USER TO DETAILS
public sendUsername(n: string){
    this.detailsSrv.buttonClickSubject.next(n);
};
}
