import { Component } from '@angular/core';
import { FieldArrayType, FormlyFormBuilder } from '@ngx-formly/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'formly-array-type',
  template: `
    <legend *ngIf="to.label">{{ to.label }}</legend>
    <mat-divider></mat-divider>
    <div *ngFor="let field of field.fieldGroup; let i = index">
        <div class="row">
            <formly-field
                class="col s12 m10"
                [field]="field"
                [options]="options"
                [form]="formControl">
            </formly-field>
            <div class="col s12 m2">
                <a id="remove-button" mat-raised-button color="primary" (click)="remove(i)">Remove</a>
            </div>
        </div>
        <mat-divider></mat-divider>
    </div>
    <div class="row">
        <div class="col s12 m10">
        </div>
        <div class="col s12 m2 text-right">
            <a id="add-button" mat-raised-button color="warn" (click)="add()">Add</a>
        </div>
    </div>
  `,
  styleUrls: ['./array.type.css']
})
export class ArrayTypeComponent extends FieldArrayType {
  constructor(builder: FormlyFormBuilder) {
        super(builder);
  }

}
