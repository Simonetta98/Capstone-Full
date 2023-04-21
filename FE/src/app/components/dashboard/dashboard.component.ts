import { Component, ElementRef, OnInit, QueryList, ViewChildren, ViewEncapsulation } from '@angular/core';
import { AuthData, AuthService } from 'src/app/services/auth.service';
import { CommentRes, LocalItem, PatchDto, Post, PostRes, User, UserProfile } from 'src/app/models/models';
import { HttpService } from 'src/app/services/http.service';
import { urlBaseBE } from 'src/environments/environment';
import { Observable} from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { PostService } from 'src/app/services/post.service';
import { ProfileService } from 'src/app/services/profile.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CommentService } from 'src/app/services/comment.service';
import { DetailsService } from 'src/app/services/details.service';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DashboardComponent implements OnInit {

  @ViewChildren('divComments', {read: ElementRef})
  divComments!: QueryList<ElementRef>;

  commentForm!:FormGroup;
  modifyCommentForm!:FormGroup;

  posts: PostRes[] = [];
  users: User[] = [];
  liked: PostRes[] = [];
  postLikes: UserProfile[] = [];
  comments: CommentRes[] = [];

//POSTS
  comment?: CommentRes;
  commentId?: number;
  commentText?: string;
  postId?: number;
  localUser?: LocalItem;
  currUser?: User;
  profile?: UserProfile;
  isClicked = true;
  submitted = false;

//SEARCH
  searchText?: string;
  usernames: String[] = [];
  filteredUsernames: any[] = [];

  getAllPostUrl = urlBaseBE + `/api/post/all/0`;

  constructor(private http: HttpService, private as: AuthService, private userSrv: UserService, private postSrv: PostService, private profileSrv: ProfileService, private commentSrv: CommentService, private detailsSrv: DetailsService) { }

  ngOnInit(): void {
   this.getAllPost();
   this.display();

   this.commentForm=new FormGroup({
    comment: new FormControl(null, Validators.required)
  });

  this.modifyCommentForm=new FormGroup({
    text: new FormControl(null, Validators.required)
  });
};


  getAllPost(){
   this.http.get(this.getAllPostUrl).subscribe(res=>{
   console.log(res);
   this.posts = res.content as PostRes[];
   this.posts.forEach(el => {
    this.displayComments(el)
    if(this.currUser)
    el.user = this.currUser
    this.findProfileByPost(el)
    this.profileSrv.getProfilePostLikes(el.id).subscribe(like=>{
        el.likes = like.length;
        //console.log(el.likes)
        this.postLikes = like as UserProfile[];
        this.checkLiked();
        this.postLikes.forEach(profile => {
            this.userSrv.getUserByProfile(profile.id).subscribe(res=>{
                profile.user = res as User;
            })
        });
        //console.log(el)
      });
    });
  })
};

  public display(){
    this.getCurrentUser().subscribe(()=>{
        this.getProfile().subscribe();
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

//POSTS AND COMMENTS
public leaveComment(){
        let value={
            text: this.commentForm.controls['comment'].value
        }
        console.log(value);
        if(this.profile && this.postId)
        this.postSrv.createComment(this.postId, this.profile.id, value).subscribe(res=>{
            //console.log(res)
            this.comment = res as CommentRes;
            this.getAllPost();
        })
        this.submitted = true;
};
public takeComment(c: number, t: string){
    this.commentId = c;
    this.commentText = t;
}

public modifyComment(){
    let modal: HTMLDivElement|null = document.querySelector("#modifyCommentModal")
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
      })
      this.getAllPost();
      this.submitted = true;
};

public displayComments(p: PostRes){
    this.postSrv.getAllComments(p.id).subscribe(res=>{
        //console.log(res);
        p.comments = res as CommentRes[];
        this.comments = p.comments;
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

public findProfileByPost(p: PostRes){
 this.profileSrv.getProfileByPost(p.id).subscribe(res=>{
    //console.log(res)
    this.userSrv.getUserByProfile(res.id).subscribe(res=>{
        p.user = res as User;
    })
 })
};

public refreshSubmitted(){
    this.submitted = false;
};

public takeId(p: number){
this.postId = p;
//console.log(this.postId)
};

public removeComment(commentId: number, postId: number){
 this.postSrv.deleteComment(commentId, postId).subscribe(res=>{
    //console.log(res);
    this.getAllPost();
});
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
this.getAllPost();
})
};
public checkLiked(){
if(this.profile)
this.profileSrv.getProfileLikedPosts(this.profile.id).subscribe(res=>{
    this.liked = res as PostRes[];
})
};

public removeLike(p:number){
if(this.profile)
this.profileSrv.deleteLike(this.profile.id, p).subscribe(res=>{
//console.log(res);
this.getAllPost();
})
};

//SEND USER TO DETAILS
public sendUsername(n: string){
    this.detailsSrv.buttonClickSubject.next(n);
};

}
