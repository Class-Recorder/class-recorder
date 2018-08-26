import { Course } from './Course';

export class YoutubeVideo {
    id: number;
    youtubeId: string;
    title: string;
    imageLink: string;
    link: string;
    description: string;
    tags: string[];
    course: Course;
}
