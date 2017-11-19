import { Course } from "../Course";
import { User } from "./User";

export class Student extends User{
    subscribed: Course[];
}