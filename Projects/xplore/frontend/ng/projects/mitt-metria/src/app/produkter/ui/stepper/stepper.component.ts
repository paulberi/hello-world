import {
  Component,
  EventEmitter,
  Input, OnChanges,
  OnInit,
  Output, SimpleChanges
} from '@angular/core';

@Component({
  selector: 'mm-stepper',
  templateUrl: './stepper.component.html',
  styleUrls: ['./stepper.component.scss']
})
export class StepperComponent implements OnInit, OnChanges {
  @Input() steps: Step[] = [];
  @Input() activeStep: number;
  @Input() title: string;
  @Output() cancelOrder = new EventEmitter();
  @Output() clickNumber = new EventEmitter();

  lastFinishedStep: number;

  constructor() {
  }

  ngOnInit(): void {
    if (this.steps.length < 2) {
      this.steps = [];
    }
    this.lastFinishedStep = this.activeStep - 1;
  }

  ngOnChanges(changes: SimpleChanges) {
    this.lastFinishedStep = this.activeStep - 1;
  }

  isActive(stepBeingRendered: Step): boolean {
    return stepBeingRendered.stepNumber === this.activeStep;
  }

  isInactive(stepBeingRendered: Step) {
    return stepBeingRendered.stepNumber > this.activeStep;
  }

  isLatestDone(stepBeingRendered: Step): boolean {
    return stepBeingRendered.stepNumber === this.lastFinishedStep;
  }

  isDone(stepBeingRendered: Step): boolean {
    return stepBeingRendered.stepNumber < this.activeStep;
  }

  isLastStep(stepBeingRendered: Step) {
    return stepBeingRendered.stepNumber === this.steps.length;
  }

  isNextStepActive(stepBeingRendered: Step) {
    return stepBeingRendered.stepNumber + 1 === this.activeStep;
  }

  isNextStepInactive(stepBeingRendered: Step) {
    return stepBeingRendered.stepNumber + 1 > this.activeStep;
  }
}


export interface Step {
  stepNumber: number;
  title: string;
}
