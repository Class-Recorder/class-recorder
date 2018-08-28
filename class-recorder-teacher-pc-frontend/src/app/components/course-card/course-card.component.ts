import { Component, OnInit, Inject, Output, EventEmitter } from '@angular/core';
import { Input } from '@angular/core';
import { Course } from '../../classes/Course';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material';
import { CourseService } from '../../services/course.service';

@Component({
    selector: 'app-course-card',
    templateUrl: './course-card.component.html',
    styleUrls: ['./course-card.component.css']
})
export class CourseCardComponent implements OnInit {

    @Input()
    course: Course;

    @Output('videoDeleted')
    videoDeleted: EventEmitter<any> = new EventEmitter<any>();

    constructor(private dialog: MatDialog,
        private courseService: CourseService) {}

    ngOnInit() {
    }

    openDialogDelete(courseId: number) {
        const dialogRef = this.dialog.open(DialogDeleteCourseComponent, {
            width: '80%',
            data: { answer: true }
        });
        dialogRef.afterClosed().subscribe(result => {
            if (result !== undefined) {
                console.log(result);
                this.courseService.deleteCourseById(courseId).subscribe((data) => {
                    this.videoDeleted.emit(data);
                });
            }
        });
    }

}

@Component({
    selector: 'app-dialog-delete-course',
    templateUrl: 'dialog-delete-course.html',
    styleUrls: ['./course-card.component.css']
})
export class DialogDeleteCourseComponent {

    constructor(
        public dialogRef: MatDialogRef<DialogDeleteCourseComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any) {}

    onNoClick(): void {
        this.dialogRef.close();
    }

    onYesClick(): void {
        this.dialogRef.close();
    }
}
