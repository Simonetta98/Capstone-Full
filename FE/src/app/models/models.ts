
export interface User{
    id: number,
    name: string,
    username: string,
    email: string
}
export interface LocalItem{
    username: string,
    accessToken: string,
    tokenType: string
}
export interface UserProfile{
    id: number,
    birthdate: Date,
    description: string,
    gender: string,
    github: string,
    linkedin: string,
    user: User
}
export interface Post{
    title: string,
    description: string,
    github: string
}
export interface Comment{
    text: string
}
export interface PostRes{
    id: number,
    user: User,
    title: string,
    description: string,
    pType: string,
    likes: number,
    date: string,
    isLiked: string,
    github: string,
    comments: CommentRes[]
}
export interface CommentRes{
    id: number,
    userProfile: UserProfile,
    text: string,
    name: string,
    user: User
}
export enum PType{
    PUBLIC = "PUBLIC",
    PRIVATE = "PRIVATE"
}

export enum Gender{
    MALE = "MALE",
    FEMALE = "FEMALE",
    OTHER = "OTHER"
}
export interface PatchDto{
    op: string,
    key: string,
    value: string
}

