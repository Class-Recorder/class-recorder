import { FormlyJsonschema } from '@ngx-formly/core/json-schema/public_api';

export default (formlyJsonschema: FormlyJsonschema) => {
    return [formlyJsonschema.toFieldConfig({
        'title': 'Video cuts',
        'type': 'object',
        'required': [
          'cuts',
        ],
        'properties': {
            'cuts': {
                'type': 'array',
                'title': 'Cuts',
                'items': {
                    'type': 'object',
                    'required': [
                        'start',
                        'end'
                    ],
                    'properties': {
                        'start': {
                            'type': 'string',
                            'pattern': /(?:[01]\d|2[0123]):(?:[012345]\d):(?:[012345]\d)/,
                            'title': 'Start',
                            'description': 'Cut start',
                        },
                        'end': {
                            'type': 'string',
                            'pattern': /(?:[01]\d|2[0123]):(?:[012345]\d):(?:[012345]\d)/,
                            'title': 'End',
                            'description': 'Cut end',
                        }
                    },
                },
            },
        },
    })];
};
