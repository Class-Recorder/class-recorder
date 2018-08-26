import {Teacher} from './user/Teacher';
import { YoutubeVideo } from './Video';
import { Student } from './user/Student';

export class Course {
    id: number;
    name: string;
    description: string;
    teacherCreators: Teacher[];
    videos: YoutubeVideo[];
    subscribers: Student[];
}
