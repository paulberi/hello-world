import { style, animate, trigger, transition } from '@angular/animations';

export const cartAnimation = trigger('transformCart', [
    transition(
        ':enter',
        [
            style({ opacity: 0, transform: 'scale(0.8)', transformOrigin: "top right" }),
            animate('120ms cubic-bezier(0, 0, 0.2, 1)',
                style({ opacity: 1, transform: 'scale(1)' }))
        ]
    ),
    transition(
        ':leave',
        [
            style({ opacity: 1 }),
            animate('150ms ease-in',
                style({ opacity: 0 }))
        ]
    )
]);