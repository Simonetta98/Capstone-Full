import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { CommentRes, LocalItem, PatchDto, PostRes, User, UserProfile } from 'src/app/models/models';
import { CommentService } from 'src/app/services/comment.service';
import { DetailsService } from 'src/app/services/details.service';
import { PostService } from 'src/app/services/post.service';
import { ProfileService } from 'src/app/services/profile.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss']
})
export class DetailsComponent implements OnInit {

  localUser?: LocalItem;
  currUser?: User;
  currProfile?: UserProfile;

  user?: User;
  postId?: number;
  profile?: UserProfile;
  posts: PostRes[] = [];
  comment?: CommentRes;
  commentId?: number;
  commentText?: string;
  submitted = false;

  commentForm!:FormGroup;
  modifyCommentForm!:FormGroup;

  constructor(private detailsSrv: DetailsService, private profileSrv: ProfileService, private userSrv: UserService, private postSrv: PostService, private commentSrv: CommentService ) { }

  ngOnInit(): void {
    this.getCurrentUser();
    this.displayData();

    this.commentForm=new FormGroup({
        comment: new FormControl(null, Validators.required)
    });

    this.modifyCommentForm=new FormGroup({
        text: new FormControl(null, Validators.required)
    });
  }

  public displayData(){
    this.detailsSrv.buttonClick$.subscribe(res=>{
        this.findUserDetails(res as string);
    })
  };
  public findUserDetails(username: string){
    this.profileSrv.getProfileByUsername(username).subscribe(res=>{
        this.profile = res as UserProfile;
        //console.log(this.profile)
        this.userSrv.getUserByProfile(this.profile.id).subscribe(res=>{
            this.user = res as User;
            //console.log(this.user.)
            this.findUserPosts();
        })
    })
  };
  public findUserPosts(){
    if(this.profile)
    this.profileSrv.getProfilePosts(this.profile?.id).subscribe(res=>{
        this.posts = res as PostRes[];
        //console.log(this.posts)
        this.posts.forEach(el => {
            this.displayComments(el);
            this.profileSrv.getProfilePostLikes(el.id).subscribe(like=>{
                el.likes = like.length;
        });
      })
    })
  };

  //CURRENT USER
  public getLocalUser(): void{
    const userJson = localStorage.getItem('user');
    if (!userJson) {
        return;
    }
    this.localUser = JSON.parse(userJson);
  };

  public getCurrentUser(){
        this.getLocalUser();
        console.log("USER_USERNAME: " + this.localUser?.username);
        if(this.localUser){
         this.userSrv.getUserByUsername(this.localUser.username).subscribe(res=>{
            this.currUser = res as User;
             //console.log(this.currUser);
             this.profileSrv.getProfileById(this.currUser.id).subscribe(res=>{
                this.currProfile = res as UserProfile;
             })
         });
       }
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
        if(this.currProfile && this.postId)
        this.postSrv.createComment(this.postId, this.currProfile.id, value).subscribe(res=>{
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
      })
      this.displayData();
      this.submitted = true;
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
   if(this.currProfile)
   this.profileSrv.createLike(this.currProfile.id, p).subscribe(res=>{
    //console.log(res);
    this.displayData();
   })
  };

  public removeLike(p:number){
   if(this.currProfile)
   this.profileSrv.deleteLike(this.currProfile.id, p).subscribe(res=>{
    //console.log(res);
    this.displayData();
   })
  };

}
