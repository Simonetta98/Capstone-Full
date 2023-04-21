import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, ElementRef, OnInit, QueryList, ViewChildren } from '@angular/core';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { Observable, Subscriber} from 'rxjs';
import { CommentRes, Gender, LocalItem, PType, PatchDto, Post, PostRes, User, UserProfile } from 'src/app/models/models';
import { CommentService } from 'src/app/services/comment.service';
import { DetailsService } from 'src/app/services/details.service';
import { PostService } from 'src/app/services/post.service';
import { ProfileService } from 'src/app/services/profile.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {

  @ViewChildren('divComments', {read: ElementRef})
  divComments!: QueryList<ElementRef>;

  commentForm!:FormGroup;
  modifyCommentForm!:FormGroup;

  addForm!:FormGroup;
  infoForm!:FormGroup;
  linkedinForm!:FormGroup;
  githubForm!:FormGroup;
  submitted = false;

  comments: CommentRes[] = [];
  comment?: CommentRes;
  postId?: number;
  commentId?: number;
  commentText?: string;

  users: User[] = [];
  posts: PostRes[] = [];
  likes: UserProfile[] = [];
  postLikes: UserProfile[] = [];
  liked: PostRes[] = [];
  types = PType;
  genders = Gender;

  getValues(obj: any) { return Object.values(obj) };
  getGenders(obj: any) { return Object.values(obj) };

  currUser?: User;
  localUser?: LocalItem;
  profile?: UserProfile;


  constructor(private userSrv: UserService, private postSrv: PostService, private profileSrv: ProfileService, private commentSrv: CommentService, private detailsSrv: DetailsService) { }

  ngOnInit(): void {
    this.getUsers();
    this.displayData();

    this.addForm=new FormGroup({
        title: new FormControl(null, Validators.required),
        description: new FormControl(null, Validators.required),
        github: new FormControl(null, Validators.required)
    });

    this.infoForm=new FormGroup({
        birthdate: new FormControl(null, Validators.required),
        description: new FormControl(null, Validators.required),
        github: new FormControl(null, Validators.required),
        linkedin: new FormControl(null, Validators.required),
        gender: new FormControl(null, Validators.required)
    });

    this.commentForm=new FormGroup({
        comment: new FormControl(null, Validators.required)
    });

    this.modifyCommentForm=new FormGroup({
        text: new FormControl(null, Validators.required)
    });

    this.linkedinForm=new FormGroup({
        linkedin: new FormControl(null, Validators.required)
    });
    this.githubForm=new FormGroup({
        github: new FormControl(null, Validators.required)
    });
  }

  //display current data
  public displayData(){
  this.getProfile().subscribe(()=>{
       this.checkLiked();
        if(this.profile)
        this.profileSrv.getProfilePosts(this.profile.id).subscribe(res=>{
            //console.log(res)
            this.posts = res as PostRes[];
            //order by id
            this.posts = this.posts.sort((a, b) => a.id - b.id);
            //console.log(this.posts)
            this.posts.forEach(el => {
                this.displayComments(el)
                if(this.currUser)
                el.user = this.currUser
                this.profileSrv.getProfilePostLikes(el.id).subscribe(like=>{
                    el.likes = like.length;
                    this.postLikes = like as UserProfile[];
                    //console.log(el.likes)
                    //console.log(el)
                    this.postLikes.forEach(profile => {
                        this.userSrv.getUserByProfile(profile.id).subscribe(res=>{
                            profile.user = res as User;
                        })
                    });
                });
            });
        })
     })
  };

  // 1- prendi lo user da local storage
  public getLocalUser(): void{
    const userJson = localStorage.getItem('user');
    if (!userJson) {
        return;
    }
    this.localUser = JSON.parse(userJson);
  };

  // 2- prendi lo user loggato
  public getCurrentUser(){
    return new Observable((subscriber)=>{
        this.getLocalUser();
        console.log("USER_USERNAME: " + this.localUser?.username);
        if(this.localUser){
         this.userSrv.getUserByUsername(this.localUser.username).subscribe(res=>{
            subscriber.next(this.currUser = res as User);
            subscriber.complete();
             //console.log(this.currUser);
         });
       }
    })
  };

  //prendi tutti gli users
  public getUsers(): void {
    this.userSrv.getAllUsers().subscribe(res=>{
        this.users = res.content;
        //console.log(this.users);
     }),
     (error: HttpErrorResponse)=>{
         alert(error.message);
     }
  };

  public getProfile() {
    return new Observable((subscriber)=>{
    this.getCurrentUser().subscribe(()=>{
        if(this.currUser)
        this.profileSrv.getProfileByUsername(this.currUser.username).subscribe(res=>{
            console.log(res)
            subscriber.next(this.profile = res as UserProfile);
            subscriber.complete();
        });
      })
    })
  };

  //POSTS
  public onPost(){
    let value={
        title: this.addForm.controls['title'].value,
        description: this.addForm.controls['description'].value,
        github: this.addForm.controls['github'].value
    }
    console.log(value);
    if(this.profile)
    this.postSrv.createPost(value, this.profile.id).subscribe(res=>{
        console.log(res)
        this.displayData()
    })
    this.submitted = true;
  };

  public deletePost(p: number){
    this.postSrv.deletePost(p).subscribe(res=>{
    //console.log(res);
       this.displayData()
    })
  }

  //PROFILE INFO
  public onProfile(){
    let value={
        birthdate: this.infoForm.controls['birthdate'].value,
        description: this.infoForm.controls['description'].value,
        github: this.infoForm.controls['github'].value,
        linkedin: this.infoForm.controls['linkedin'].value,
        gender: this.infoForm.controls['gender'].value
    }
    const patchBirthdate: PatchDto = {
        op: "update",
        key: "birthdate",
        value: String(value.birthdate)
      };
      const patchDescription: PatchDto = {
        op: "update",
        key: "description",
        value: value.description
      };
      const patchGithub: PatchDto = {
        op: "update",
        key: "github",
        value: value.github
      };
      const patchLinkedin: PatchDto = {
        op: "update",
        key: "linkedin",
        value: value.linkedin
      };
      const patchGender: PatchDto = {
        op: "update",
        key: "gender",
        value: value.gender
      };

    if(this.profile)
    this.profileSrv.modifyProfile(this.profile.id, patchBirthdate).subscribe(res=>{
        //console.log(res)
        if(this.profile)
    this.profileSrv.modifyProfile(this.profile.id, patchDescription).subscribe(res=>{
            //console.log(res)
        if(this.profile)
    this.profileSrv.modifyProfile(this.profile.id, patchGithub).subscribe(res=>{
        //console.log(res)
        if(this.profile)
    this.profileSrv.modifyProfile(this.profile.id, patchLinkedin).subscribe(res=>{
        //console.log(res)
        if(this.profile)
    this.profileSrv.modifyProfile(this.profile.id, patchLinkedin).subscribe(res=>{
        //console.log(res)
        if(this.profile)
    this.profileSrv.modifyProfile(this.profile.id, patchGender).subscribe(res=>{
        //console.log(patchGender)
        this.submitted = true;
    this.displayData();
            })
          })
        })
      })
    })
    })
  };

  public modifyLinkedin(){
    let value={
        linkedin: this.linkedinForm.controls['linkedin'].value
    }
    const patchLink: PatchDto = {
        op: "update",
        key: "linkedin",
        value: value.linkedin
      };
    if(this.profile)
    this.profileSrv.modifyProfile(this.profile.id, patchLink).subscribe(res=>{
        //console.log(res)
        this.submitted = true;
    this.displayData();
    })
  };

  public modifyGithub(){
    let value={
        github: this.githubForm.controls['github'].value
    }
    const patchLink: PatchDto = {
        op: "update",
        key: "github",
        value: value.github
      };
    if(this.profile)
    this.profileSrv.modifyProfile(this.profile.id, patchLink).subscribe(res=>{
        //console.log(res)
        this.submitted = true;
    this.displayData();
    })
  };

  //COMMENTS
    public takeId(p: number){
    this.postId = p;
    //console.log(this.postId)
    };

    public removeComment(commentId: number, postId: number){
    this.postSrv.deleteComment(commentId, postId).subscribe(res=>{
        //console.log(res);
        this.displayData();
    });
    };
    public leaveComment(){
        let value={
            text: this.commentForm.controls['comment'].value
        }
        console.log(value);
        if(this.profile && this.postId)
        this.postSrv.createComment(this.postId, this.profile.id, value).subscribe(res=>{
            //console.log(res)
            this.comment = res as CommentRes;
            this.displayData();
        })
        this.submitted = true;
};
public displayComments(p: PostRes){
    this.postSrv.getAllComments(p.id).subscribe(res=>{
        //console.log(res);
        p.comments = res as CommentRes[];
        //order by id
        p.comments = p.comments.sort((a, b) => a.id - b.id);
        p.comments.forEach(el => {
            let profile: UserProfile = el.userProfile as UserProfile;
            this.userSrv.getUserByProfile(profile.id).subscribe(res=>{
                let user: User = res as User;
                el.name = user.name
                el.user = user
            })
        })
    });
};

public takeComment(c: number, t: string){
    this.commentId = c;
    this.commentText = t;
}

public modifyComment(){
    let value={
        text: this. modifyCommentForm.controls['text'].value
    }
    const patchText: PatchDto = {
        op: "update",
        key: "text",
        value: value.text
      };
      if(this.commentId)
      this.commentSrv.modifyComment(this.commentId, patchText).subscribe(res=>{
          //console.log(res)
          this.submitted = true;
    this.displayData();
      })
};
public refreshSubmitted(){
    this.submitted = false;
};

  //LIKES
  public likeUnlike(p: PostRes){
    if(p.isLiked == null) this.giveLike(p.id);
    else this.removeLike(p.id);
  };

  public giveLike(p: number){
   if(this.profile)
   this.profileSrv.createLike(this.profile.id, p).subscribe(res=>{
    //console.log(res);
    this.displayData();
   })
  };
  public checkLiked(){
    if(this.profile)
    this.profileSrv.getProfileLikedPosts(this.profile.id).subscribe(res=>{
        this.liked = res as PostRes[];
        this.liked.forEach(el => {
            this.profileSrv.getProfileByPost(el.id).subscribe(res=>{
                let profile = res as UserProfile;
                this.userSrv.getUserByProfile(profile.id).subscribe(res=>{
                    let user = res as User;
                    el.user = user;
                })
            })
        });
    })
  };

  public removeLike(p:number){
   if(this.profile)
   this.profileSrv.deleteLike(this.profile.id, p).subscribe(res=>{
    //console.log(res);
    this.displayData();
   })
  };

  //SEND USER TO DETAILS
public sendUsername(n: string){
    this.detailsSrv.buttonClickSubject.next(n);
};

}
