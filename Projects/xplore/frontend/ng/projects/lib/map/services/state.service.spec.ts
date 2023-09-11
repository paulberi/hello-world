import { inject, TestBed } from '@angular/core/testing'
import {StateService} from "./state.service";

describe('Service: StateService', () => {
    let service: StateService;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
            ],
            providers: [
                StateService,
            ],
        });

        service = TestBed.get(StateService);

        // Mock implementation of console.error to
        // return undefined to stop printing out to console log during test
        jest.spyOn(console, 'error').mockImplementation(() => undefined)
    });
    it('should create an instance successfully', () => {
        expect(service).toBeDefined()
    })
});