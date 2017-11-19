import {Teacher} from './user/Teacher';
import { Video } from './Video';
import { Student } from './user/Student';

export class Course {
    id: number;
    name: string;
    description: string;
    teacherCreators: Teacher[];
    videos: Video[];
    subscribers: Student[];
}