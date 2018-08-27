import { Component } from '@angular/core';
import { FieldArrayType, FormlyFormBuilder } from '@ngx-formly/core';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'formly-array-type',
  template: `
    <legend *ngIf="to.label">{{ to.label }}</legend>
    <div *ngFor="let field of field.fieldGroup; let i = index" class="row">
      <formly-field
        class="col s10 m10"
        [field]="field"
        [options]="options"
        [form]="formControl">
      </formly-field>
      <div class="col-sm-2">
        <a mat-raised-button color="primary" (click)="remove(i)">Remove</a>
      </div>
    </div>
    <div class="text-right">
      <a mat-raised-button color="warn" (click)="add()">Add</a>
    </div>
  `,
})
export class ArrayTypeComponent extends FieldArrayType {
  constructor(builder: FormlyFormBuilder) {
        super(builder);
  }

}
