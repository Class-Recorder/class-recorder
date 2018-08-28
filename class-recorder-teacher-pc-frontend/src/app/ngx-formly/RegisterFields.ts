import { FormlyFieldConfig } from '@ngx-formly/core';

export default [
    {
        key: 'userName',
        type: 'input',
        templateOptions: {
            label: 'User Name (without spaces)',
            pattern: /^[\S]*$/,
            required: true,
        },
        validation: {
            messages: {
                pattern: (error, field: FormlyFieldConfig) => `User Name must not have spaces`
            },
        }
    },
    {
        key: 'password',
        type: 'input',
        templateOptions: {
            label: 'Password',
            type: 'password',
            required: true
        }
    },
    {
        key: 'email',
        type: 'input',
        templateOptions: {
            label: 'Email',
            type: 'email',
            pattern: /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/,
            required: true
        }
    },
    {
        key: 'fullName',
        type: 'input',
        templateOptions: {
            label: 'Full Name',
            required: true
        }
    }
];
