import { FormlyFieldConfig } from '@ngx-formly/core';

export default [
    {
        key: 'videoTitle',
        type: 'input',
        templateOptions: {
            label: 'Video Title',
            required: true
        }
    },
    {
        key: 'description',
        type: 'textarea',
        templateOptions: {
            label: 'Description',
            required: true,
            rows: 5
        }
    },
    {
        key: 'tags',
        type: 'textarea',
        templateOptions: {
            label: 'Tags (separated by commas)',
            pattern: /^[\w\s]+(?:,[\w\s]*)*$/,
            required: false,
            rows: 2
        },
        validation: {
            messages: {
                pattern: (error, field: FormlyFieldConfig) => `Tags must be phrases separated by commas`
            },
        }
    }
];

