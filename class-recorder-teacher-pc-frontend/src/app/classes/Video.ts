import { Course } from "./Course";

export class Video {
    id: number;
    youtubeId: string;
    title: string;
    description: string;
    tags: string;
    course: Course;
}