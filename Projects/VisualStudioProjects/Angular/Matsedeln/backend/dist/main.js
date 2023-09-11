(window["webpackJsonp"] = window["webpackJsonp"] || []).push([["main"],{

/***/ "/QED":
/*!**************************************************!*\
  !*** ./src/app/pages/header/header.component.ts ***!
  \**************************************************/
/*! exports provided: HeaderComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "HeaderComponent", function() { return HeaderComponent; });
/* harmony import */ var src_app_models_user__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! src/app/models/user */ "2hxB");
/* harmony import */ var rxjs_operators__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! rxjs/operators */ "kU1M");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var src_app_services_municipality_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! src/app/services/municipality.service */ "ZLEw");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/router */ "tyNb");
/* harmony import */ var src_app_services_date_handler_service__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! src/app/services/date-handler.service */ "D2Hn");
/* harmony import */ var src_app_services_menu_service__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! src/app/services/menu.service */ "Gi7S");
/* harmony import */ var src_app_services_sharing_service__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! src/app/services/sharing.service */ "KxZz");
/* harmony import */ var src_assets_alert__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! src/assets/alert */ "X440");
/* harmony import */ var _auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! @auth0/auth0-angular */ "2beD");
/* harmony import */ var _angular_material_select__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(/*! @angular/material/select */ "d3UM");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_11__ = __webpack_require__(/*! @angular/forms */ "3Pt+");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_12__ = __webpack_require__(/*! @angular/common */ "ofXK");
/* harmony import */ var _angular_material_core__WEBPACK_IMPORTED_MODULE_13__ = __webpack_require__(/*! @angular/material/core */ "FKr1");














function HeaderComponent_mat_option_7_Template(rf, ctx) { if (rf & 1) {
    const _r5 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](0, "mat-option", 10);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("click", function HeaderComponent_mat_option_7_Template_mat_option_click_0_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵrestoreView"](_r5); const ctx_r4 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵnextContext"](); return ctx_r4.chooseMunicipality(); });
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](1);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
} if (rf & 2) {
    const municipality_r3 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("value", municipality_r3);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](1);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtextInterpolate1"](" ", municipality_r3.municipalityName, "");
} }
function HeaderComponent_ng_template_10_mat_option_0_Template(rf, ctx) { if (rf & 1) {
    const _r9 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](0, "mat-option", 10);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("click", function HeaderComponent_ng_template_10_mat_option_0_Template_mat_option_click_0_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵrestoreView"](_r9); const ctx_r8 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵnextContext"](2); return ctx_r8.chooseSchool(); });
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](1);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
} if (rf & 2) {
    const school_r7 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("value", school_r7);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](1);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtextInterpolate"](school_r7.schoolName);
} }
function HeaderComponent_ng_template_10_Template(rf, ctx) { if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtemplate"](0, HeaderComponent_ng_template_10_mat_option_0_Template, 2, 2, "mat-option", 5);
} if (rf & 2) {
    const ctx_r1 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵnextContext"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("ngForOf", ctx_r1.chosenMunicipality.schools);
} }
function HeaderComponent_ng_template_12_mat_option_0_Template(rf, ctx) { if (rf & 1) {
    const _r13 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](0, "mat-option", 10);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("click", function HeaderComponent_ng_template_12_mat_option_0_Template_mat_option_click_0_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵrestoreView"](_r13); const ctx_r12 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵnextContext"](2); return ctx_r12.setWeek(); });
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](1);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
} if (rf & 2) {
    const week_r11 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("value", week_r11);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](1);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtextInterpolate"]("Vecka " + week_r11.weekNr);
} }
function HeaderComponent_ng_template_12_Template(rf, ctx) { if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtemplate"](0, HeaderComponent_ng_template_12_mat_option_0_Template, 2, 2, "mat-option", 5);
} if (rf & 2) {
    const ctx_r2 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵnextContext"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("ngForOf", ctx_r2.weeks);
} }
class HeaderComponent {
    constructor(municipalityService, router, dateHandlerService, menuService, sharingService, alert, auth) {
        this.municipalityService = municipalityService;
        this.router = router;
        this.dateHandlerService = dateHandlerService;
        this.menuService = menuService;
        this.sharingService = sharingService;
        this.alert = alert;
        this.auth = auth;
        this.subscriptions = [];
        this.weekTitle = "Vecka";
        this.currentWeek = this.dateHandlerService.getCurrentWeek();
    }
    ngOnInit() {
        this.auth.isAuthenticated$.subscribe((loggedIn) => {
            if (loggedIn) {
                this.subscriptions.push(this.auth.user$.subscribe((user) => {
                    let currentUser = new src_app_models_user__WEBPACK_IMPORTED_MODULE_0__["User"]();
                    currentUser.setUserFromAuthPic(user.picture);
                    if (!currentUser.permissions.some((perm) => perm === 'admin')) {
                        this.$municipalities = this.filterMunicipalitiesFromUserPerm(currentUser);
                    }
                    else {
                        this.$municipalities = this.municipalityService.getMunicipalities();
                    }
                }));
            }
            else {
                this.$municipalities = this.municipalityService.getMunicipalities();
            }
        });
    }
    filterMunicipalitiesFromUserPerm(currentUser) {
        return this.municipalityService.getMunicipalities().pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_1__["map"])((municipalities) => {
            let mun = municipalities.filter((mun) => {
                return mun.schools.some((school) => {
                    return currentUser.schoolIds.some((schoolId) => schoolId === school._id);
                });
            });
            mun.forEach((municipality) => {
                municipality.schools = municipality.schools.filter((school) => {
                    return currentUser.schoolIds.some((schoolId) => schoolId === school._id);
                });
            });
            return mun;
        }));
    }
    ngOnDestroy() {
        this.subscriptions.forEach(sub => {
            sub.unsubscribe();
        });
    }
    chooseMunicipality() {
        this.sharingService.setWeek(null);
        this.weeks = [];
        this.nextWeek = null;
        this.previousWeek = null;
        this.previousWeekTitle = '';
        this.nextWeekTitle = '';
        this.weekTitle = "Vecka";
    }
    chooseSchool() {
        this.sharingService.setWeek(null);
        this.weeks = [];
        this.nextWeek = null;
        this.previousWeek = null;
        this.previousWeekTitle = '';
        this.nextWeekTitle = '';
        let school = this.chosenSchool;
        if (school._menuId === '' || school._menuId === undefined) {
            this.alert.showAlert('', 'Vald skola har ingen matsedel!', 'error');
        }
        else {
            let menu;
            let sub = this.menuService.getMenu(school._menuId).subscribe((menuu) => {
                menu = menuu;
            }, (err) => {
            }, () => {
                this.sharingService.setMenu(menu);
                this.weeks = this.dateHandlerService.getWeeks(menu);
                this.weeks.forEach(week => {
                    if (week.weekNr === this.currentWeek) {
                        this.chosenWeek = week;
                        this.chooseWeek(this.chosenWeek);
                    }
                });
            });
            this.subscriptions.push(sub);
        }
    }
    setWeek() {
        this.chooseWeek(this.chosenWeek);
    }
    chooseWeek(week) {
        this.chosenWeek = week;
        this.weekTitle = "Vecka " + this.chosenWeek.weekNr;
        this.previousWeek = this.dateHandlerService.getPreviousWeek(this.weeks, week);
        if (this.previousWeek) {
            this.previousWeekTitle = "V." + this.previousWeek.weekNr;
        }
        else {
            this.previousWeekTitle = "";
        }
        this.nextWeek = this.dateHandlerService.getNextWeek(this.weeks, week);
        if (this.nextWeek) {
            this.nextWeekTitle = "V." + this.nextWeek.weekNr;
        }
        else {
            this.nextWeekTitle = "";
        }
        this.sharingService.setWeek(this.chosenWeek);
    }
    previousWeekClick() {
        if (this.previousWeek) {
            this.chooseWeek(this.previousWeek);
        }
    }
    nextWeekClick() {
        if (this.nextWeek) {
            this.chooseWeek(this.nextWeek);
        }
    }
}
HeaderComponent.ɵfac = function HeaderComponent_Factory(t) { return new (t || HeaderComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](src_app_services_municipality_service__WEBPACK_IMPORTED_MODULE_3__["MunicipalityService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](_angular_router__WEBPACK_IMPORTED_MODULE_4__["Router"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](src_app_services_date_handler_service__WEBPACK_IMPORTED_MODULE_5__["DateHandlerService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](src_app_services_menu_service__WEBPACK_IMPORTED_MODULE_6__["MenuService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](src_app_services_sharing_service__WEBPACK_IMPORTED_MODULE_7__["SharingService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](src_assets_alert__WEBPACK_IMPORTED_MODULE_8__["Alert"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](_auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_9__["AuthService"])); };
HeaderComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdefineComponent"]({ type: HeaderComponent, selectors: [["app-header"]], decls: 19, vars: 10, consts: [[1, "centered-content"], [1, "paginatorLeft", 3, "click"], [2, "font-size", "30px"], [1, "weekPager"], ["required", "", "placeholder", "Kommun", 3, "ngModel", "ngModelChange"], ["class", "dropdown-item", 3, "value", "click", 4, "ngFor", "ngForOf"], ["required", "", "placeholder", "Skola", 3, "ngModel", "ngModelChange"], [3, "ngIf"], ["required", "", "placeholder", "Vecka", 3, "ngModel", "ngModelChange"], [1, "paginatorRight", 3, "click"], [1, "dropdown-item", 3, "value", "click"]], template: function HeaderComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](0, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](1, "button", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("click", function HeaderComponent_Template_button_click_1_listener() { return ctx.previousWeekClick(); });
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](2, "span", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](3, " < ");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](4, "h1", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](6, "mat-select", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("ngModelChange", function HeaderComponent_Template_mat_select_ngModelChange_6_listener($event) { return ctx.chosenMunicipality = $event; });
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtemplate"](7, HeaderComponent_mat_option_7_Template, 2, 2, "mat-option", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵpipe"](8, "async");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](9, "mat-select", 6);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("ngModelChange", function HeaderComponent_Template_mat_select_ngModelChange_9_listener($event) { return ctx.chosenSchool = $event; });
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtemplate"](10, HeaderComponent_ng_template_10_Template, 1, 1, "ng-template", 7);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](11, "mat-select", 8);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("ngModelChange", function HeaderComponent_Template_mat_select_ngModelChange_11_listener($event) { return ctx.chosenWeek = $event; });
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtemplate"](12, HeaderComponent_ng_template_12_Template, 1, 1, "ng-template", 7);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](13, "div");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](14, "button", 9);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("click", function HeaderComponent_Template_button_click_14_listener() { return ctx.nextWeekClick(); });
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](15, "h1", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](16);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](17, "span", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](18, " > ");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtextInterpolate1"](" ", ctx.previousWeekTitle, " ");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](1);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("ngModel", ctx.chosenMunicipality);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](1);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("ngForOf", _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵpipeBind1"](8, 8, ctx.$municipalities));
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("ngModel", ctx.chosenSchool);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](1);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("ngIf", ctx.chosenMunicipality);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](1);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("ngModel", ctx.chosenWeek);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](1);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("ngIf", ctx.chosenSchool);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](4);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtextInterpolate1"](" ", ctx.nextWeekTitle, " ");
    } }, directives: [_angular_material_select__WEBPACK_IMPORTED_MODULE_10__["MatSelect"], _angular_forms__WEBPACK_IMPORTED_MODULE_11__["RequiredValidator"], _angular_forms__WEBPACK_IMPORTED_MODULE_11__["NgControlStatus"], _angular_forms__WEBPACK_IMPORTED_MODULE_11__["NgModel"], _angular_common__WEBPACK_IMPORTED_MODULE_12__["NgForOf"], _angular_common__WEBPACK_IMPORTED_MODULE_12__["NgIf"], _angular_material_core__WEBPACK_IMPORTED_MODULE_13__["MatOption"]], pipes: [_angular_common__WEBPACK_IMPORTED_MODULE_12__["AsyncPipe"]], styles: [".centered-content[_ngcontent-%COMP%] {\n  max-height: 50px;\n}\n\n.dropdown-item[_ngcontent-%COMP%] {\n  margin-top: 3px;\n  cursor: pointer;\n  background: #063752;\n  color: white;\n}\n\n.dropdown-item[_ngcontent-%COMP%]:hover {\n  color: #1775b8 !important;\n}\n\n.paginatorLeft[_ngcontent-%COMP%], .paginatorRight[_ngcontent-%COMP%] {\n  display: flex;\n  margin-left: 100px;\n  margin-right: 100px;\n  cursor: pointer;\n  color: white;\n  border: 0px;\n  background: transparent;\n  color: white;\n}\n\n.paginatorLeft[_ngcontent-%COMP%]   .weekPager[_ngcontent-%COMP%], .paginatorRight[_ngcontent-%COMP%]   .weekPager[_ngcontent-%COMP%] {\n  margin-top: 8px;\n  font-size: 10;\n}\n\n.paginatorLeft[_ngcontent-%COMP%]:active, .paginatorRight[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\nmat-select[_ngcontent-%COMP%] {\n  margin-top: 5px;\n  background: transparent !important;\n  color: white !important;\n  cursor: pointer;\n  padding: 10px;\n  font-size: 14px;\n  font-family: Roboto, \"Helvetica Neue\", sans-serif;\n  max-width: 160px;\n}\n\n  .mat-select-placeholder {\n  color: white !important;\n}\n\n  .mat-select-min-line {\n  color: white !important;\n}\n\n.mat-option.mat-selected[_ngcontent-%COMP%] {\n  background: #063752 !important;\n  color: #1775b8 !important;\n}\n\n  .mat-select-panel {\n  background: #063752;\n}\n\n  .mat-select-arrow {\n  color: white !important;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uL2hlYWRlci5jb21wb25lbnQuc2NzcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTtFQUNFLGdCQUFBO0FBQ0Y7O0FBRUE7RUFDRSxlQUFBO0VBQ0EsZUFBQTtFQUNBLG1CQUFBO0VBQ0EsWUFBQTtBQUNGOztBQUFFO0VBQ00seUJBQUE7QUFFUjs7QUFFQTtFQUNFLGFBQUE7RUFDQSxrQkFBQTtFQUNBLG1CQUFBO0VBQ0EsZUFBQTtFQUNBLFlBQUE7RUFLQSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxZQUFBO0FBSEY7O0FBSEU7RUFDRSxlQUFBO0VBQ0EsYUFBQTtBQUtKOztBQUFFO0VBQ0UsMEJBQUE7QUFFSjs7QUFFQTtFQUNFLGVBQUE7RUFDQSxrQ0FBQTtFQUNBLHVCQUFBO0VBQ0EsZUFBQTtFQUNBLGFBQUE7RUFDQSxlQUFBO0VBQ0EsaURBQUE7RUFDQSxnQkFBQTtBQUNGOztBQUVBO0VBQ0UsdUJBQUE7QUFDRjs7QUFFQTtFQUNFLHVCQUFBO0FBQ0Y7O0FBRUE7RUFDRSw4QkFBQTtFQUNBLHlCQUFBO0FBQ0Y7O0FBRUE7RUFDRSxtQkFBQTtBQUNGOztBQUVBO0VBQ0UsdUJBQUE7QUFDRiIsImZpbGUiOiJoZWFkZXIuY29tcG9uZW50LnNjc3MiLCJzb3VyY2VzQ29udGVudCI6WyIuY2VudGVyZWQtY29udGVudCB7XG4gIG1heC1oZWlnaHQ6IDUwcHg7XG59XG5cbi5kcm9wZG93bi1pdGVtIHtcbiAgbWFyZ2luLXRvcDogM3B4O1xuICBjdXJzb3I6IHBvaW50ZXI7XG4gIGJhY2tncm91bmQ6IzA2Mzc1MjtcbiAgY29sb3I6IHdoaXRlO1xuICAmOmhvdmVyIHtcbiAgICAgICAgY29sb3I6ICMxNzc1YjggIWltcG9ydGFudDtcbiAgICAgIH1cbn1cblxuLnBhZ2luYXRvckxlZnQsIC5wYWdpbmF0b3JSaWdodHtcbiAgZGlzcGxheTogZmxleDtcbiAgbWFyZ2luLWxlZnQ6IDEwMHB4O1xuICBtYXJnaW4tcmlnaHQ6IDEwMHB4O1xuICBjdXJzb3I6IHBvaW50ZXI7XG4gIGNvbG9yOiBoc2woMCwgMCUsIDEwMCUpO1xuICAud2Vla1BhZ2VyIHtcbiAgICBtYXJnaW4tdG9wOiA4cHg7XG4gICAgZm9udC1zaXplOiAxMDtcbiAgfVxuICBib3JkZXI6IDBweDtcbiAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgJjphY3RpdmUge1xuICAgIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xuICB9XG59XG5cbm1hdC1zZWxlY3Qge1xuICBtYXJnaW4tdG9wOiA1cHg7XG4gIGJhY2tncm91bmQ6dHJhbnNwYXJlbnQgIWltcG9ydGFudDtcbiAgY29sb3I6d2hpdGUgIWltcG9ydGFudDtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICBwYWRkaW5nOjEwcHg7XG4gIGZvbnQtc2l6ZTogMTRweDtcbiAgZm9udC1mYW1pbHk6IFJvYm90bywgXCJIZWx2ZXRpY2EgTmV1ZVwiLCBzYW5zLXNlcmlmO1xuICBtYXgtd2lkdGg6IDE2MHB4O1xufVxuXG46Om5nLWRlZXAgLm1hdC1zZWxlY3QtcGxhY2Vob2xkZXJ7XG4gIGNvbG9yOiB3aGl0ZSAhaW1wb3J0YW50O1xufVxuXG46Om5nLWRlZXAgLm1hdC1zZWxlY3QtbWluLWxpbmV7XG4gIGNvbG9yOndoaXRlICFpbXBvcnRhbnQ7XG59XG5cbi5tYXQtb3B0aW9uLm1hdC1zZWxlY3RlZCB7XG4gIGJhY2tncm91bmQ6ICMwNjM3NTIgIWltcG9ydGFudDtcbiAgY29sb3I6ICMxNzc1YjggIWltcG9ydGFudDtcbn1cblxuOjpuZy1kZWVwIC5tYXQtc2VsZWN0LXBhbmVse1xuICBiYWNrZ3JvdW5kOiAjMDYzNzUyO1xufVxuXG46Om5nLWRlZXAgLm1hdC1zZWxlY3QtYXJyb3cge1xuICBjb2xvcjogd2hpdGUgIWltcG9ydGFudDtcbn1cbiJdfQ== */"] });


/***/ }),

/***/ 0:
/*!***************************!*\
  !*** multi ./src/main.ts ***!
  \***************************/
/*! no static exports found */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__(/*! /Users/paulberinyuylukong/VisualStudioProjects/Angular/Matsedeln/frontend/src/main.ts */"zUnb");


/***/ }),

/***/ "2hxB":
/*!********************************!*\
  !*** ./src/app/models/user.ts ***!
  \********************************/
/*! exports provided: User */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "User", function() { return User; });
class User {
    constructor() {
        this.menuIds = new Array();
    }
    setUserFromAuthPic(user) {
        this._id = user._id;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
        this.schoolIds = user.schoolIds;
        this.permissions = user.permissions;
        if (user.menuId) {
            this.menuIds = user.menuId;
        }
    }
}


/***/ }),

/***/ "33AS":
/*!**************************************************!*\
  !*** ./src/app/pages/footer/footer.component.ts ***!
  \**************************************************/
/*! exports provided: FooterComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "FooterComponent", function() { return FooterComponent; });
/* harmony import */ var src_environments_environment__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! src/environments/environment */ "AytR");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @auth0/auth0-angular */ "2beD");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/common */ "ofXK");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/router */ "tyNb");





function FooterComponent_ng_container_2_Template(rf, ctx) { if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementContainerStart"](0);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](1, "div", 5);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](2, "i", 6);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](3, "div", 7);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](4, "p", 8);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](5);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementContainerEnd"]();
} if (rf & 2) {
    const ctx_r0 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵnextContext"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](5);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtextInterpolate1"](" ", ctx_r0.loggedInUser, " ");
} }
function FooterComponent_ng_container_5_Template(rf, ctx) { if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementContainerStart"](0);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](1, "button", 9);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](2, " Administrera anv\u00E4ndare ");
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementContainerEnd"]();
} }
function FooterComponent_ng_container_6_Template(rf, ctx) { if (rf & 1) {
    const _r6 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementContainerStart"](0);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](1, "button", 10);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](2, " Administrera matsedel ");
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](3, "button", 11);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵlistener"]("click", function FooterComponent_ng_container_6_Template_button_click_3_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵrestoreView"](_r6); const ctx_r5 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵnextContext"](); return ctx_r5.auth.logout({ returnTo: ctx_r5.ROOT_URL + "/logout" }); });
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](4, " Logga ut ");
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementContainerEnd"]();
} }
function FooterComponent_ng_template_8_Template(rf, ctx) { if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](0, "button", 12);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](1, " Logga in ");
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
} }
class FooterComponent {
    constructor(auth) {
        this.auth = auth;
        this.admin = false;
        this.ROOT_URL = src_environments_environment__WEBPACK_IMPORTED_MODULE_0__["environment"].ROOT_URL;
    }
    ngOnInit() {
        // this.sub = this.auth.user$.subscribe((user) => {
        //   let currentUser = new User();
        //   currentUser.setUserFromAuthPic(user.picture);
        //   this.admin = currentUser.permissions.some((perm)=> perm === 'admin');
        //   this.loggedInUser = currentUser.firstName + ' ' + currentUser.lastName;
        // })
    }
    ngOnDestroy() {
        this.sub.unsubscribe();
    }
}
FooterComponent.ɵfac = function FooterComponent_Factory(t) { return new (t || FooterComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](_auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_2__["AuthService"])); };
FooterComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineComponent"]({ type: FooterComponent, selectors: [["app-footer"]], decls: 10, vars: 8, consts: [[1, "one"], [1, "user-content"], [4, "ngIf"], [4, "ngIf", "ngIfElse"], ["loggedOut", ""], [1, "icon"], [1, "fas", "fa-user", 2, "color", "white"], [1, "two"], [1, "logged-in-user-label"], ["routerLink", "/user", 1, "user-button"], ["routerLink", "/admin", 1, "admin-button"], [1, "admin-button", 3, "click"], ["routerLink", "/login", 1, "admin-button"]], template: function FooterComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](0, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](1, "div", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtemplate"](2, FooterComponent_ng_container_2_Template, 6, 1, "ng-container", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵpipe"](3, "async");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](4, "div");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtemplate"](5, FooterComponent_ng_container_5_Template, 3, 0, "ng-container", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtemplate"](6, FooterComponent_ng_container_6_Template, 5, 0, "ng-container", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵpipe"](7, "async");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtemplate"](8, FooterComponent_ng_template_8_Template, 2, 0, "ng-template", null, 4, _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtemplateRefExtractor"]);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    } if (rf & 2) {
        const _r3 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](9);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("ngIf", _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵpipeBind1"](3, 4, ctx.auth.isAuthenticated$));
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](3);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("ngIf", ctx.admin);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](1);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("ngIf", _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵpipeBind1"](7, 6, ctx.auth.isAuthenticated$))("ngIfElse", _r3);
    } }, directives: [_angular_common__WEBPACK_IMPORTED_MODULE_3__["NgIf"], _angular_router__WEBPACK_IMPORTED_MODULE_4__["RouterLink"]], pipes: [_angular_common__WEBPACK_IMPORTED_MODULE_3__["AsyncPipe"]], styles: ["@import url(\"https://use.fontawesome.com/releases/v5.14.0/css/all.css\");\n.admin-button[_ngcontent-%COMP%] {\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  padding: 20px;\n}\n.admin-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n.user-button[_ngcontent-%COMP%] {\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  padding: 20px;\n}\n.user-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n.one[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: row;\n}\n.two[_ngcontent-%COMP%] {\n  margin-top: 4px;\n}\n.user-content[_ngcontent-%COMP%] {\n  position: fixed;\n  left: 20px;\n  margin-top: 20px;\n  display: flex;\n  flex-direction: row;\n}\n.logged-in-user-label[_ngcontent-%COMP%] {\n  color: white;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uL2Zvb3Rlci5jb21wb25lbnQuc2NzcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBUSx1RUFBQTtBQUVSO0VBQ0UsV0FBQTtFQUNBLHVCQUFBO0VBQ0EsWUFBQTtFQUlBLGVBQUE7RUFDQSxhQUFBO0FBSEY7QUFERTtFQUNJLDBCQUFBO0FBR047QUFHQTtFQUNFLFdBQUE7RUFDQSx1QkFBQTtFQUNBLFlBQUE7RUFJQSxlQUFBO0VBQ0EsYUFBQTtBQUhGO0FBREU7RUFDSSwwQkFBQTtBQUdOO0FBR0E7RUFDRSxhQUFBO0VBQ0EsbUJBQUE7QUFBRjtBQUdBO0VBQ0UsZUFBQTtBQUFGO0FBR0E7RUFDRSxlQUFBO0VBQ0EsVUFBQTtFQUNBLGdCQUFBO0VBQ0EsYUFBQTtFQUNBLG1CQUFBO0FBQUY7QUFHQTtFQUNFLFlBQUE7QUFBRiIsImZpbGUiOiJmb290ZXIuY29tcG9uZW50LnNjc3MiLCJzb3VyY2VzQ29udGVudCI6WyJAaW1wb3J0IHVybChcImh0dHBzOi8vdXNlLmZvbnRhd2Vzb21lLmNvbS9yZWxlYXNlcy92NS4xNC4wL2Nzcy9hbGwuY3NzXCIpO1xuXG4uYWRtaW4tYnV0dG9uIHtcbiAgYm9yZGVyOiAwcHg7XG4gIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICBjb2xvcjogd2hpdGU7XG4gICY6YWN0aXZlIHtcbiAgICAgIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xuICB9XG4gIGN1cnNvcjogcG9pbnRlcjtcbiAgcGFkZGluZzogMjBweDtcbn1cblxuLnVzZXItYnV0dG9uIHtcbiAgYm9yZGVyOiAwcHg7XG4gIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICBjb2xvcjogd2hpdGU7XG4gICY6YWN0aXZlIHtcbiAgICAgIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xuICB9XG4gIGN1cnNvcjogcG9pbnRlcjtcbiAgcGFkZGluZzogMjBweDtcbn1cblxuLm9uZSB7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiByb3c7XG59XG5cbi50d28ge1xuICBtYXJnaW4tdG9wOiA0cHg7XG59XG5cbi51c2VyLWNvbnRlbnQge1xuICBwb3NpdGlvbjogZml4ZWQ7XG4gIGxlZnQ6IDIwcHg7XG4gIG1hcmdpbi10b3A6IDIwcHg7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiByb3c7XG59XG5cbi5sb2dnZWQtaW4tdXNlci1sYWJlbCB7XG4gIGNvbG9yOiB3aGl0ZTtcbn1cbiJdfQ== */"] });


/***/ }),

/***/ "7exz":
/*!*****************************************************************************!*\
  !*** ./src/app/pages/admin/admin-user/create-user/create-user.component.ts ***!
  \*****************************************************************************/
/*! exports provided: CreateUserComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CreateUserComponent", function() { return CreateUserComponent; });
/* harmony import */ var src_app_models_user__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! src/app/models/user */ "2hxB");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var src_app_services_municipality_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! src/app/services/municipality.service */ "ZLEw");
/* harmony import */ var src_app_services_user_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! src/app/services/user.service */ "qfBg");
/* harmony import */ var src_assets_alert__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! src/assets/alert */ "X440");
/* harmony import */ var _auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! @auth0/auth0-angular */ "2beD");
/* harmony import */ var _user_header_user_header_component__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ../user-header/user-header.component */ "ySEZ");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! @angular/forms */ "3Pt+");
/* harmony import */ var ng_multiselect_dropdown__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! ng-multiselect-dropdown */ "Egam");









class CreateUserComponent {
    constructor(municipalityService, userService, alert, auth) {
        this.municipalityService = municipalityService;
        this.userService = userService;
        this.alert = alert;
        this.auth = auth;
        this.dropdownSettings = {};
        this.schoolsTitle = 'Välj skolor till användare';
        this.subscriptions = [];
    }
    ngOnInit() {
        this.subscriptions.push(this.auth.user$.subscribe((user) => {
            this.currentUser = new src_app_models_user__WEBPACK_IMPORTED_MODULE_0__["User"]();
            this.currentUser.setUserFromAuthPic(user.picture);
        }));
        this.subscriptions.push(this.municipalityService.getSchools().subscribe((schools) => {
            this.schoolsToChoose = schools;
        }, (err) => this.alert.showAlert('Error', 'Skolor kunde inte hämtas från databasen', 'error')));
        this.dropdownSettings = {
            singleSelection: false,
            textField: 'schoolName',
            selectAllText: 'Markera alla',
            unSelectAllText: 'Avmarkera alla',
            searchPlaceholderText: 'Sök',
            itemsShowLimit: 1,
            idField: '_id',
            allowSearchFilter: true
        };
    }
    ngOnDestroy() {
        this.subscriptions.forEach((sub) => {
            sub.unsubscribe();
        });
    }
    createUser(firstName, lastName, email, password, admin, schools) {
        if (!this.currentUser.permissions.some((permission) => permission === 'admin')) {
            this.alert.showAlert('', 'Du måste ha behörighet för att administrera användare!', 'error');
        }
        else {
            if (lastName.length < 1) {
                this.alert.showAlert('', 'Användare måste ha ett efternamn. Testa igen!', 'error');
            }
            else if (email.length < 5) {
                this.alert.showAlert('', 'Användare måste ha en email på minst 5 tecken. Testa igen!', 'error');
            }
            else if (password.length < 5) {
                this.alert.showAlert('', 'Användare måste ha ett lösenord på minst 5 tecken. Testa igen!', 'error');
            }
            else {
                let newUser = new src_app_models_user__WEBPACK_IMPORTED_MODULE_0__["User"]();
                newUser.setUserFromAuthPic({ 'firstName': firstName, 'lastName': lastName, 'email': email, 'permissions': [], 'schoolIds': [], 'menuIds': [] });
                let schoolIds = [];
                newUser.password = password;
                if (admin) {
                    newUser.permissions.push('admin');
                }
                else if (schools) {
                    schools.forEach(school => {
                        schoolIds.push(school.id);
                    });
                }
                newUser.schoolIds = schoolIds;
                let sub = this.userService.postUser(newUser).subscribe(() => {
                }, (err) => this.alert.showAlert('Nånting gick fel.', 'Användaren sparades inte', 'error'), () => this.alert.showAlertAndUpdatePage('Sparad!', 'Användaren har blivit sparad.', 'success'));
                this.subscriptions.push(sub);
            }
            ;
        }
    }
    ;
    clickAdmin(adminChecked) {
        if (adminChecked) {
            this.selectedSchools = this.schoolsToChoose;
        }
        else {
            this.selectedSchools = [];
        }
    }
}
CreateUserComponent.ɵfac = function CreateUserComponent_Factory(t) { return new (t || CreateUserComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](src_app_services_municipality_service__WEBPACK_IMPORTED_MODULE_2__["MunicipalityService"]), _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](src_app_services_user_service__WEBPACK_IMPORTED_MODULE_3__["UserService"]), _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](src_assets_alert__WEBPACK_IMPORTED_MODULE_4__["Alert"]), _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](_auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_5__["AuthService"])); };
CreateUserComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineComponent"]({ type: CreateUserComponent, selectors: [["app-create-user"]], decls: 31, vars: 4, consts: [[1, "centered-content"], [1, "choices-content"], [1, "form-content"], [1, "fields-content"], [1, "element"], ["type", "text", "placeholder", "Ange f\u00F6rnamn:", 1, "input-field"], ["firstName", ""], ["type", "text", "placeholder", "Ange efternamn:", 1, "input-field"], ["lastName", ""], ["type", "email", "placeholder", "Ange email:", 1, "input-field"], ["email", ""], ["type", "password", "placeholder", "Ange l\u00F6senord:", 1, "input-field"], ["password", ""], [1, "other-content"], [1, "permissions-label"], ["title", "Admin", "type", "checkbox", "value", "admin", "name", "admin", "id", "admin", 1, "permission-checkbox", 3, "change"], ["admin", ""], [1, "admin-label"], ["name", "schools", 1, "school-dropdown", 3, "placeholder", "data", "ngModel", "settings", "ngModelChange"], ["schools", ""], [1, "button-content"], [1, "create-button", 3, "click"]], template: function CreateUserComponent_Template(rf, ctx) { if (rf & 1) {
        const _r6 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵgetCurrentView"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](0, "app-user-header");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](1, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](2, "div", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](3, "form", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](4, "div", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](5, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](6, "input", 5, 6);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](8, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](9, "input", 7, 8);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](11, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](12, "input", 9, 10);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](14, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](15, "input", 11, 12);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](17, "div", 13);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](18, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](19, "label", 14);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](20, "Beh\u00F6righeter: ");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](21, "input", 15, 16);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵlistener"]("change", function CreateUserComponent_Template_input_change_21_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵrestoreView"](_r6); const _r4 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](22); return ctx.clickAdmin(_r4.checked); });
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](23, "label", 17);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](24, "Admin");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](25, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](26, "ng-multiselect-dropdown", 18, 19);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵlistener"]("ngModelChange", function CreateUserComponent_Template_ng_multiselect_dropdown_ngModelChange_26_listener($event) { return ctx.selectedSchools = $event; });
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](28, "div", 20);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](29, "button", 21);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵlistener"]("click", function CreateUserComponent_Template_button_click_29_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵrestoreView"](_r6); const _r0 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](7); const _r1 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](10); const _r2 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](13); const _r3 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](16); const _r4 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](22); const _r5 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](27); return ctx.createUser(_r0.value, _r1.value, _r2.value, _r3.value, _r4.checked, _r5.selectedItems); });
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](30, " Spara anv\u00E4ndare ");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](26);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("placeholder", "V\u00E4lj skolor att tilldela")("data", ctx.schoolsToChoose)("ngModel", ctx.selectedSchools)("settings", ctx.dropdownSettings);
    } }, directives: [_user_header_user_header_component__WEBPACK_IMPORTED_MODULE_6__["UserHeaderComponent"], _angular_forms__WEBPACK_IMPORTED_MODULE_7__["ɵangular_packages_forms_forms_ba"], _angular_forms__WEBPACK_IMPORTED_MODULE_7__["NgControlStatusGroup"], _angular_forms__WEBPACK_IMPORTED_MODULE_7__["NgForm"], ng_multiselect_dropdown__WEBPACK_IMPORTED_MODULE_8__["MultiSelectComponent"], _angular_forms__WEBPACK_IMPORTED_MODULE_7__["NgControlStatus"], _angular_forms__WEBPACK_IMPORTED_MODULE_7__["NgModel"]], styles: [".centered-content[_ngcontent-%COMP%] {\n  height: 620px;\n}\n\n.choices-content[_ngcontent-%COMP%] {\n  max-height: 250px;\n  min-height: 250px;\n  justify-content: left;\n}\n\n.form-content[_ngcontent-%COMP%] {\n  display: flex;\n}\n\n.fields-content[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: column;\n  margin-top: 20px;\n}\n\n.other-content[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: column;\n  margin-top: 20px;\n  margin-left: 50px;\n}\n\n.input-field[_ngcontent-%COMP%] {\n  background: #08496d;\n  border: 0px;\n  border-radius: 8px;\n  color: white;\n  width: 250px;\n  height: 18px;\n  margin: 10px;\n  padding: 8px;\n}\n\n.navbar-item[_ngcontent-%COMP%] {\n  cursor: pointer;\n  max-width: 250px;\n  margin-bottom: 10px;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%] {\n  color: white;\n  background: transparent;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%]:hover {\n  color: #1775b8;\n}\n\n.navbar-dropdown[_ngcontent-%COMP%] {\n  height: 210px;\n}\n\n.permissions-label[_ngcontent-%COMP%] {\n  color: white;\n  height: 20px;\n  margin-left: 12px;\n}\n\n.permission-checkbox[_ngcontent-%COMP%] {\n  margin-left: 20px;\n}\n\n.admin-label[_ngcontent-%COMP%] {\n  color: white;\n  height: 20px;\n  margin-left: 5px;\n}\n\n.create-button[_ngcontent-%COMP%] {\n  width: 150px;\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  margin-top: 95px;\n}\n\n.create-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.button-content[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: row;\n  align-items: center;\n  margin-left: 100px;\n}\n\n.school-dropdown[_ngcontent-%COMP%] {\n  color: white;\n  cursor: pointer;\n}\n\n.multiselect-parent[_ngcontent-%COMP%] {\n  width: 300px;\n}\n\n.multiselect-parent[_ngcontent-%COMP%]   .dropdown-toggle[_ngcontent-%COMP%] {\n  width: 300px;\n}\n\n.multiselect-parent[_ngcontent-%COMP%]   .dropdown-menu[_ngcontent-%COMP%] {\n  width: 300px;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uL2NyZWF0ZS11c2VyLmNvbXBvbmVudC5zY3NzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBO0VBQ0UsYUFBQTtBQUNGOztBQUVBO0VBQ0UsaUJBQUE7RUFDQSxpQkFBQTtFQUNBLHFCQUFBO0FBQ0Y7O0FBRUE7RUFDRSxhQUFBO0FBQ0Y7O0FBRUE7RUFDRSxhQUFBO0VBQ0Esc0JBQUE7RUFDQSxnQkFBQTtBQUNGOztBQUVBO0VBQ0UsYUFBQTtFQUNBLHNCQUFBO0VBQ0EsZ0JBQUE7RUFDQSxpQkFBQTtBQUNGOztBQUVBO0VBQ0UsbUJBQUE7RUFDQSxXQUFBO0VBQ0Esa0JBQUE7RUFDQSxZQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7RUFDQSxZQUFBO0VBQ0EsWUFBQTtBQUNGOztBQUVBO0VBQ0UsZUFBQTtFQVFBLGdCQUFBO0VBQ0EsbUJBQUE7QUFORjs7QUFGRTtFQUNJLFlBQUE7RUFDQSx1QkFBQTtBQUlOOztBQUhNO0VBQ0ksY0FBQTtBQUtWOztBQUVBO0VBQ0UsYUFBQTtBQUNGOztBQUVBO0VBQ0UsWUFBQTtFQUNBLFlBQUE7RUFDQSxpQkFBQTtBQUNGOztBQUVBO0VBQ0UsaUJBQUE7QUFDRjs7QUFFQTtFQUNFLFlBQUE7RUFDQSxZQUFBO0VBQ0EsZ0JBQUE7QUFDRjs7QUFFQTtFQUNFLFlBQUE7RUFDQSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxZQUFBO0VBSUEsZUFBQTtFQUNBLGdCQUFBO0FBRkY7O0FBRkU7RUFDRSwwQkFBQTtBQUlKOztBQUVBO0VBQ0UsYUFBQTtFQUNBLG1CQUFBO0VBQ0EsbUJBQUE7RUFDQSxrQkFBQTtBQUNGOztBQUVBO0VBQ0UsWUFBQTtFQUNBLGVBQUE7QUFDRjs7QUFFQTtFQUNFLFlBQUE7QUFDRjs7QUFFQTtFQUNFLFlBQUE7QUFDRjs7QUFFQTtFQUNFLFlBQUE7QUFDRiIsImZpbGUiOiJjcmVhdGUtdXNlci5jb21wb25lbnQuc2NzcyIsInNvdXJjZXNDb250ZW50IjpbIi5jZW50ZXJlZC1jb250ZW50IHtcbiAgaGVpZ2h0OiA2MjBweDtcbn1cblxuLmNob2ljZXMtY29udGVudCB7XG4gIG1heC1oZWlnaHQ6IDI1MHB4O1xuICBtaW4taGVpZ2h0OiAyNTBweDtcbiAganVzdGlmeS1jb250ZW50OiBsZWZ0O1xufVxuXG4uZm9ybS1jb250ZW50IHtcbiAgZGlzcGxheTogZmxleDtcbn1cblxuLmZpZWxkcy1jb250ZW50IHtcbiAgZGlzcGxheTogZmxleDtcbiAgZmxleC1kaXJlY3Rpb246IGNvbHVtbjtcbiAgbWFyZ2luLXRvcDogMjBweDtcbn1cblxuLm90aGVyLWNvbnRlbnQge1xuICBkaXNwbGF5OiBmbGV4O1xuICBmbGV4LWRpcmVjdGlvbjogY29sdW1uO1xuICBtYXJnaW4tdG9wOiAyMHB4O1xuICBtYXJnaW4tbGVmdDogNTBweDtcbn1cblxuLmlucHV0LWZpZWxkIHtcbiAgYmFja2dyb3VuZDogIzA4NDk2ZDtcbiAgYm9yZGVyOiAwcHg7XG4gIGJvcmRlci1yYWRpdXM6IDhweDtcbiAgY29sb3I6IHdoaXRlO1xuICB3aWR0aDogMjUwcHg7XG4gIGhlaWdodDogMThweDtcbiAgbWFyZ2luOiAxMHB4O1xuICBwYWRkaW5nOiA4cHg7XG59XG5cbi5uYXZiYXItaXRlbSB7XG4gIGN1cnNvcjogcG9pbnRlcjtcbiAgLm5hdmJhci1saW5rIHtcbiAgICAgIGNvbG9yOiBoc2woMCwgMCUsIDEwMCUpO1xuICAgICAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gICAgICAmOmhvdmVyIHtcbiAgICAgICAgICBjb2xvcjogIzE3NzViODtcbiAgICAgIH1cbiAgfVxuICBtYXgtd2lkdGg6IDI1MHB4O1xuICBtYXJnaW4tYm90dG9tOiAxMHB4O1xufVxuXG4ubmF2YmFyLWRyb3Bkb3duIHtcbiAgaGVpZ2h0OiAyMTBweDtcbn1cblxuLnBlcm1pc3Npb25zLWxhYmVsIHtcbiAgY29sb3I6IHdoaXRlO1xuICBoZWlnaHQ6IDIwcHg7XG4gIG1hcmdpbi1sZWZ0OiAxMnB4O1xufVxuXG4ucGVybWlzc2lvbi1jaGVja2JveCB7XG4gIG1hcmdpbi1sZWZ0OiAyMHB4O1xufVxuXG4uYWRtaW4tbGFiZWwge1xuICBjb2xvcjogd2hpdGU7XG4gIGhlaWdodDogMjBweDtcbiAgbWFyZ2luLWxlZnQ6IDVweDtcbn1cblxuLmNyZWF0ZS1idXR0b24ge1xuICB3aWR0aDogMTUwcHg7XG4gIGJvcmRlcjogMHB4O1xuICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgY29sb3I6IHdoaXRlO1xuICAmOmFjdGl2ZSB7XG4gICAgdHJhbnNmb3JtOiB0cmFuc2xhdGVZKDRweCk7XG4gIH1cbiAgY3Vyc29yOiBwb2ludGVyO1xuICBtYXJnaW4tdG9wOiA5NXB4O1xufVxuXG4uYnV0dG9uLWNvbnRlbnQge1xuICBkaXNwbGF5OiBmbGV4O1xuICBmbGV4LWRpcmVjdGlvbjogcm93O1xuICBhbGlnbi1pdGVtczogY2VudGVyO1xuICBtYXJnaW4tbGVmdDogMTAwcHg7XG59XG5cbi5zY2hvb2wtZHJvcGRvd24ge1xuICBjb2xvcjp3aGl0ZTtcbiAgY3Vyc29yOiBwb2ludGVyO1xufVxuXG4ubXVsdGlzZWxlY3QtcGFyZW50IHtcbiAgd2lkdGg6IDMwMHB4O1xufVxuXG4ubXVsdGlzZWxlY3QtcGFyZW50IC5kcm9wZG93bi10b2dnbGUge1xuICB3aWR0aDogMzAwcHg7XG59XG5cbi5tdWx0aXNlbGVjdC1wYXJlbnQgLmRyb3Bkb3duLW1lbnUge1xuICB3aWR0aDogMzAwcHg7XG59XG4iXX0= */"] });


/***/ }),

/***/ "AytR":
/*!*****************************************!*\
  !*** ./src/environments/environment.ts ***!
  \*****************************************/
/*! exports provided: environment */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "environment", function() { return environment; });
// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
const environment = {
    production: false,
    ROOT_URL: 'http://localhost:8080'
};
/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.


/***/ }),

/***/ "B4DE":
/*!**********************************************!*\
  !*** ./src/app/pages/menu/menu.component.ts ***!
  \**********************************************/
/*! exports provided: MenuComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MenuComponent", function() { return MenuComponent; });
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var src_app_services_sharing_service__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! src/app/services/sharing.service */ "KxZz");
/* harmony import */ var _header_header_component__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../header/header.component */ "/QED");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/common */ "ofXK");
/* harmony import */ var _day_day_component__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./day/day.component */ "a0hZ");





function MenuComponent_div_1_div_3_Template(rf, ctx) { if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](0, "div", 4);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelement"](1, "app-day", 5);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
} if (rf & 2) {
    const day_r2 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵadvance"](1);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵproperty"]("day", day_r2);
} }
function MenuComponent_div_1_Template(rf, ctx) { if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](0, "div");
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](1, "div", 1);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](2, "div", 2);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtemplate"](3, MenuComponent_div_1_div_3_Template, 2, 1, "div", 3);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
} if (rf & 2) {
    const ctx_r0 = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵnextContext"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵadvance"](3);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵproperty"]("ngForOf", ctx_r0.week.days);
} }
class MenuComponent {
    constructor(sharingService) {
        this.sharingService = sharingService;
        this.subscriptions = [];
    }
    ngOnInit() {
        let sub = this.sharingService.getObservableWeek().subscribe((week) => {
            this.week = week;
        });
        this.subscriptions.push(sub);
    }
    ngOnDestroy() {
        this.subscriptions.forEach(sub => {
            sub.unsubscribe();
        });
    }
}
MenuComponent.ɵfac = function MenuComponent_Factory(t) { return new (t || MenuComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdirectiveInject"](src_app_services_sharing_service__WEBPACK_IMPORTED_MODULE_1__["SharingService"])); };
MenuComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineComponent"]({ type: MenuComponent, selectors: [["app-menu"]], decls: 2, vars: 1, consts: [[4, "ngIf"], [1, "centered-content"], [1, "menu-content"], ["class", "day-div", 4, "ngFor", "ngForOf"], [1, "day-div"], [3, "day"]], template: function MenuComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelement"](0, "app-header");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtemplate"](1, MenuComponent_div_1_Template, 4, 1, "div", 0);
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵadvance"](1);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵproperty"]("ngIf", ctx.week);
    } }, directives: [_header_header_component__WEBPACK_IMPORTED_MODULE_2__["HeaderComponent"], _angular_common__WEBPACK_IMPORTED_MODULE_3__["NgIf"], _angular_common__WEBPACK_IMPORTED_MODULE_3__["NgForOf"], _day_day_component__WEBPACK_IMPORTED_MODULE_4__["DayComponent"]], styles: ["@import url(\"https://use.fontawesome.com/releases/v5.14.0/css/all.css\");\n.centered-content[_ngcontent-%COMP%] {\n  height: 100%;\n}\n.menu-content[_ngcontent-%COMP%] {\n  margin-top: 55px;\n  max-width: 1000px;\n  display: flex;\n  flex-direction: column;\n  width: 100%;\n  height: 100%;\n  vertical-align: middle;\n  align-items: center;\n  justify-content: center;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uL21lbnUuY29tcG9uZW50LnNjc3MiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQVEsdUVBQUE7QUFDUjtFQUNJLFlBQUE7QUFDSjtBQUVBO0VBQ0ksZ0JBQUE7RUFDQSxpQkFBQTtFQUNBLGFBQUE7RUFDQSxzQkFBQTtFQUNBLFdBQUE7RUFDQSxZQUFBO0VBQ0Esc0JBQUE7RUFDQSxtQkFBQTtFQUNBLHVCQUFBO0FBQ0oiLCJmaWxlIjoibWVudS5jb21wb25lbnQuc2NzcyIsInNvdXJjZXNDb250ZW50IjpbIkBpbXBvcnQgdXJsKFwiaHR0cHM6Ly91c2UuZm9udGF3ZXNvbWUuY29tL3JlbGVhc2VzL3Y1LjE0LjAvY3NzL2FsbC5jc3NcIik7XG4uY2VudGVyZWQtY29udGVudCB7XG4gICAgaGVpZ2h0OiAxMDAlO1xufVxuXG4ubWVudS1jb250ZW50IHtcbiAgICBtYXJnaW4tdG9wOiA1NXB4O1xuICAgIG1heC13aWR0aDogMTAwMHB4O1xuICAgIGRpc3BsYXk6IGZsZXg7XG4gICAgZmxleC1kaXJlY3Rpb246IGNvbHVtbjtcbiAgICB3aWR0aDogMTAwJTtcbiAgICBoZWlnaHQ6IDEwMCU7XG4gICAgdmVydGljYWwtYWxpZ246IG1pZGRsZTtcbiAgICBhbGlnbi1pdGVtczogY2VudGVyO1xuICAgIGp1c3RpZnktY29udGVudDogY2VudGVyO1xufVxuIl19 */"] });


/***/ }),

/***/ "D2Hn":
/*!**************************************************!*\
  !*** ./src/app/services/date-handler.service.ts ***!
  \**************************************************/
/*! exports provided: DateHandlerService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "DateHandlerService", function() { return DateHandlerService; });
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/common */ "ofXK");
/* harmony import */ var _models_week__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ../models/week */ "wUJa");
/* harmony import */ var _models_day__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ../models/day */ "KJrp");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/core */ "fXoL");




class DateHandlerService {
    constructor() { }
    getWeeks(menu) {
        let datePipe = new _angular_common__WEBPACK_IMPORTED_MODULE_0__["DatePipe"]('en-US');
        let week = new _models_week__WEBPACK_IMPORTED_MODULE_1__["Week"]();
        let weeks;
        weeks = [];
        var newEndDate = new Date(menu.endDate);
        let day;
        week.startDate = menu.startDate;
        week.weekNr = datePipe.transform(menu.startDate, 'w');
        var date = new Date(menu.startDate);
        date.setHours(0);
        for (date; date <= newEndDate; date.setDate(date.getDate() + 1)) {
            if (datePipe.transform(date, 'EEEE') === 'Monday') {
                week = new _models_week__WEBPACK_IMPORTED_MODULE_1__["Week"]();
                week.startDate = new Date(date);
                week.weekNr = datePipe.transform(date, 'w');
            }
            else if (datePipe.transform(date, 'EEEE') === 'Sunday' || datePipe.transform(date, 'mediumDate') === datePipe.transform(newEndDate, 'mediumDate')) {
                week.endDate = new Date(date);
                weeks.push(week);
            }
            if (datePipe.transform(date, 'EEEE') === 'Monday' || datePipe.transform(date, 'EEEE') === 'Tuesday' || datePipe.transform(date, 'EEEE') === 'Wednesday'
                || datePipe.transform(date, 'EEEE') === 'Thursday' || datePipe.transform(date, 'EEEE') === 'Friday') {
                day = new _models_day__WEBPACK_IMPORTED_MODULE_2__["Day"]();
                day.date = new Date(date);
                menu.meals.forEach(meal => {
                    if (datePipe.transform(meal.mealDate, 'mediumDate') === datePipe.transform(date, 'mediumDate')) {
                        day.meals.push(meal);
                    }
                });
                week.days.push(day);
            }
        }
        return weeks;
    }
    getCurrentWeek() {
        let dateNow = new Date(Date.now());
        let datePipe = new _angular_common__WEBPACK_IMPORTED_MODULE_0__["DatePipe"]('en-US');
        return datePipe.transform(dateNow, 'w');
    }
    getPreviousWeek(weeks, week) {
        let previousWeekNr = parseInt(week.weekNr) - 1;
        let returnWeek;
        weeks.forEach(week => {
            if (parseInt(week.weekNr) === previousWeekNr) {
                returnWeek = week;
            }
        });
        return returnWeek;
    }
    getNextWeek(weeks, week) {
        let nextWeek = parseInt(week.weekNr) + 1;
        let returnWeek;
        weeks.forEach(week => {
            if (parseInt(week.weekNr) === nextWeek) {
                returnWeek = week;
            }
        });
        return returnWeek;
    }
}
DateHandlerService.ɵfac = function DateHandlerService_Factory(t) { return new (t || DateHandlerService)(); };
DateHandlerService.ɵprov = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdefineInjectable"]({ token: DateHandlerService, factory: DateHandlerService.ɵfac, providedIn: 'root' });


/***/ }),

/***/ "D8EZ":
/*!************************************************!*\
  !*** ./src/app/pages/login/login.component.ts ***!
  \************************************************/
/*! exports provided: LoginComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LoginComponent", function() { return LoginComponent; });
/* harmony import */ var src_app_models_user__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! src/app/models/user */ "2hxB");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var src_app_services_user_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! src/app/services/user.service */ "qfBg");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/router */ "tyNb");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/forms */ "3Pt+");





class LoginComponent {
    constructor(userService, router) {
        this.userService = userService;
        this.router = router;
    }
    ngOnInit() {
        this.userList = [];
        this.sub = [];
        this.userService.getUsers().subscribe((users) => {
            this.userList = users;
            console.log(this.userList);
        });
    }
    login(email, password) {
        console.log(email, password, this.userList);
        let user = new src_app_models_user__WEBPACK_IMPORTED_MODULE_0__["User"]();
        user.email = email;
        user.password = password;
        console.log(user);
        this.userService.loginUser(user).subscribe((data) => {
            console.log(data);
            localStorage.removeItem('currentUser');
            localStorage.removeItem('admin');
            localStorage.setItem('currentUser', email);
            this.currentUser = data;
            this.currentUser.permissions.forEach(item => {
                if (item.toString() == "admin") {
                    localStorage.setItem('admin', 'admin');
                }
            });
            if (email == this.currentUser.email) {
                this.router.navigate['login'];
            }
        });
    }
}
LoginComponent.ɵfac = function LoginComponent_Factory(t) { return new (t || LoginComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](src_app_services_user_service__WEBPACK_IMPORTED_MODULE_2__["UserService"]), _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](_angular_router__WEBPACK_IMPORTED_MODULE_3__["Router"])); };
LoginComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineComponent"]({ type: LoginComponent, selectors: [["app-login"]], decls: 9, vars: 0, consts: [[1, "form-signin", 2, "align-items", "center", "margin-top", "40%"], ["type", "text", "id", "username", "name", "username", "placeholder", "Email", 1, "form-control", 2, "width", "20%", "margin-left", "40%", "margin-right", "40%"], ["email", ""], ["type", "password", "id", "password", "name", "password", "placeholder", "L\u00F6senord", 1, "form-control", 2, "width", "20%", "margin-left", "40%", "margin-right", "40%"], ["password", ""], [1, "btn", "btn-lg", "btn-primary", "btn-block", 2, "background-color", "rgb(18, 146, 120)", "border", "0px", "width", "15%", "margin-left", "42.5%", "margin-right", "42.5%", 3, "click"]], template: function LoginComponent_Template(rf, ctx) { if (rf & 1) {
        const _r2 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵgetCurrentView"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](0, "form", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](1, "p");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](2, "input", 1, 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](4, "p");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](5, "input", 3, 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](7, "button", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵlistener"]("click", function LoginComponent_Template_button_click_7_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵrestoreView"](_r2); const _r0 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](3); const _r1 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](6); return ctx.login(_r0.value, _r1.value); });
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](8, "Logga in");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    } }, directives: [_angular_forms__WEBPACK_IMPORTED_MODULE_4__["ɵangular_packages_forms_forms_ba"], _angular_forms__WEBPACK_IMPORTED_MODULE_4__["NgControlStatusGroup"], _angular_forms__WEBPACK_IMPORTED_MODULE_4__["NgForm"]], styles: ["\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IiIsImZpbGUiOiJsb2dpbi5jb21wb25lbnQuc2NzcyJ9 */"] });


/***/ }),

/***/ "F/c2":
/*!*********************************************!*\
  !*** ./src/app/services/web-req.service.ts ***!
  \*********************************************/
/*! exports provided: WebReqService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "WebReqService", function() { return WebReqService; });
/* harmony import */ var _environments_environment__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./../../environments/environment */ "AytR");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common/http */ "tk/3");



class WebReqService {
    constructor(http) {
        this.http = http;
        this.ROOT_URL = _environments_environment__WEBPACK_IMPORTED_MODULE_0__["environment"].ROOT_URL;
    }
    get(uri) {
        return this.http.get(`${this.ROOT_URL}/${uri}`);
    }
    post(uri, payload) {
        return this.http.post(`${this.ROOT_URL}/${uri}`, payload);
    }
    patch(uri, payload) {
        return this.http.patch(`${this.ROOT_URL}/${uri}`, payload);
    }
    delete(uri) {
        return this.http.delete(`${this.ROOT_URL}/${uri}`);
    }
}
WebReqService.ɵfac = function WebReqService_Factory(t) { return new (t || WebReqService)(_angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵinject"](_angular_common_http__WEBPACK_IMPORTED_MODULE_2__["HttpClient"])); };
WebReqService.ɵprov = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineInjectable"]({ token: WebReqService, factory: WebReqService.ɵfac, providedIn: 'root' });


/***/ }),

/***/ "Gi7S":
/*!******************************************!*\
  !*** ./src/app/services/menu.service.ts ***!
  \******************************************/
/*! exports provided: MenuService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MenuService", function() { return MenuService; });
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _web_req_service__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./web-req.service */ "F/c2");


class MenuService {
    constructor(webReqService) {
        this.webReqService = webReqService;
    }
    getMenus() {
        return this.webReqService.get('public/menu');
    }
    getMenu(_menuId) {
        return this.webReqService.get(`public/menu/${_menuId}`);
    }
    updateMenu(menu) {
        return this.webReqService.patch(`private/menu/${menu._id}`, menu);
    }
    deleteMeal(_menuId, _mealId) {
        return this.webReqService.delete(`private/menu/${_menuId}/meal/${_mealId}`);
    }
    updateMeal(meal, _menuId) {
        return this.webReqService.patch(`private/menu/${_menuId}/meal/${meal._id}`, meal);
    }
    postMeal(meal, _menuId) {
        return this.webReqService.post(`private/menu/${_menuId}/meal`, meal);
    }
    getMeal(_menuId, _mealId) {
        return this.webReqService.get(`public/menu/${_menuId}/meal/${_mealId}`);
    }
    getMenuName(_menuId) {
        return this.webReqService.get(`public/menu/name/${_menuId}`);
    }
    postMenu(menu) {
        return this.webReqService.post(`private/menu`, menu);
    }
    deleteMenu(_menuId) {
        return this.webReqService.delete(`private/menu/${_menuId}`);
    }
}
MenuService.ɵfac = function MenuService_Factory(t) { return new (t || MenuService)(_angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵinject"](_web_req_service__WEBPACK_IMPORTED_MODULE_1__["WebReqService"])); };
MenuService.ɵprov = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineInjectable"]({ token: MenuService, factory: MenuService.ɵfac, providedIn: 'root' });


/***/ }),

/***/ "JMhS":
/*!*****************************************************************************!*\
  !*** ./src/app/pages/admin/admin-user/update-user/update-user.component.ts ***!
  \*****************************************************************************/
/*! exports provided: UpdateUserComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "UpdateUserComponent", function() { return UpdateUserComponent; });
/* harmony import */ var src_app_models_user__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! src/app/models/user */ "2hxB");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var src_app_services_municipality_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! src/app/services/municipality.service */ "ZLEw");
/* harmony import */ var src_app_services_user_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! src/app/services/user.service */ "qfBg");
/* harmony import */ var src_assets_alert__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! src/assets/alert */ "X440");
/* harmony import */ var _auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! @auth0/auth0-angular */ "2beD");
/* harmony import */ var _user_header_user_header_component__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ../user-header/user-header.component */ "ySEZ");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! @angular/forms */ "3Pt+");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! @angular/common */ "ofXK");
/* harmony import */ var ng_multiselect_dropdown__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! ng-multiselect-dropdown */ "Egam");










function UpdateUserComponent_a_10_Template(rf, ctx) { if (rf & 1) {
    const _r9 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](0, "a", 27);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵlistener"]("click", function UpdateUserComponent_a_10_Template_a_click_0_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵrestoreView"](_r9); const user_r7 = ctx.$implicit; const ctx_r8 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵnextContext"](); return ctx_r8.chooseUserToUpdate(user_r7); });
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](1, "p");
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
} if (rf & 2) {
    const user_r7 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtextInterpolate"](user_r7.email);
} }
class UpdateUserComponent {
    constructor(municipalityService, userService, alert, auth) {
        this.municipalityService = municipalityService;
        this.userService = userService;
        this.alert = alert;
        this.auth = auth;
        this.dropdownSettings = {};
        this.checkAdmin = false;
        this.schoolsTitle = 'Välj skolor till användare';
        this.subscriptions = [];
        this.userToUpdateTitle = 'Välj användare att uppdatera: ';
        this.userToUpdate = new src_app_models_user__WEBPACK_IMPORTED_MODULE_0__["User"]();
        this.userToUpdate.firstName = '';
        this.userToUpdate.lastName = '';
        this.userToUpdate.email = '';
        this.userToUpdate.password = '';
    }
    ngOnInit() {
        this.subscriptions.push(this.auth.user$.subscribe((user) => {
            this.currentUser = new src_app_models_user__WEBPACK_IMPORTED_MODULE_0__["User"]();
            this.currentUser.setUserFromAuthPic(user.picture);
        }));
        this.subscriptions.push(this.userService.getUsers().subscribe((users) => {
            this.users = users;
        }));
        this.subscriptions.push(this.municipalityService.getSchools().subscribe((schools) => {
            this.schoolsToChoose = schools;
        }));
        this.dropdownSettings = {
            singleSelection: false,
            textField: 'schoolName',
            selectAllText: 'Markera alla',
            unSelectAllText: 'Avmarkera alla',
            searchPlaceholderText: 'Sök',
            itemsShowLimit: 1,
            idField: '_id',
            allowSearchFilter: true
        };
    }
    ngOnDestroy() {
        this.subscriptions.forEach((sub) => {
            sub.unsubscribe();
        });
    }
    chooseUserToUpdate(user) {
        this.checkAdmin = false;
        this.selectedSchools = [];
        this.userToUpdate = new src_app_models_user__WEBPACK_IMPORTED_MODULE_0__["User"]();
        this.userToUpdateTitle = user.email;
        this.userToUpdate._id = user._id;
        this.userToUpdate.schoolIds = user.schoolIds;
        this.userToUpdate.permissions = user.permissions;
        this.userToUpdate.menuIds = user.menuIds;
        this.userToUpdate.firstName = user.firstName;
        this.userToUpdate.lastName = user.lastName;
        this.userToUpdate.email = user.email;
        this.userToUpdate.password = user.password;
        if (user.permissions.some((permission) => permission === 'admin')) {
            this.checkAdmin = true;
            this.selectedSchools = this.schoolsToChoose;
        }
        else {
            this.selectedSchools = this.schoolsToChoose.filter((school) => {
                return this.userToUpdate.schoolIds.some((schoolId) => schoolId === school._id);
            });
        }
    }
    updateUser(firstName, lastName, email, password, admin, schools) {
        if (!this.currentUser.permissions.some((permission) => permission === 'admin')) {
            this.alert.showAlert('', 'Du måste ha behörighet för att administrera användare!', 'error');
        }
        else {
            if (lastName.length < 1) {
                this.alert.showAlert('', 'Användare måste ha ett efternamn. Testa igen!', 'error');
            }
            else if (email.length < 5) {
                this.alert.showAlert('', 'Användare måste ha en email på minst 5 tecken. Testa igen!', 'error');
            }
            else if (password.length < 5) {
                this.alert.showAlert('', 'Användare måste ha ett lösenord på minst 5 tecken. Testa igen!', 'error');
            }
            else {
                this.userToUpdate.firstName = firstName;
                this.userToUpdate.lastName = lastName;
                this.userToUpdate.email = email;
                this.userToUpdate.password = password;
                let schoolIds = [];
                this.userToUpdate.password = password;
                if (admin) {
                    this.userToUpdate.permissions.push('admin');
                }
                else if (schools) {
                    schools.forEach(school => {
                        schoolIds.push(school.id);
                    });
                }
                this.userToUpdate.schoolIds = schoolIds;
                this.subscriptions.push(this.userService.updateUser(this.userToUpdate).subscribe(() => {
                }, (err) => this.alert.showAlert('Något gick fel!', 'Användaren uppdaterades inte.', 'error'), () => this.alert.showAlertAndUpdatePage('Sparad!', 'Användaren har blivit uppdaterad.', 'success')));
            }
            ;
        }
    }
    ;
    clickAdmin(adminChecked) {
        if (adminChecked) {
            this.selectedSchools = this.schoolsToChoose;
        }
        else {
            this.selectedSchools = this.schoolsToChoose.filter((school) => {
                return this.userToUpdate.schoolIds.some((schoolId) => schoolId === school._id);
            });
        }
    }
}
UpdateUserComponent.ɵfac = function UpdateUserComponent_Factory(t) { return new (t || UpdateUserComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](src_app_services_municipality_service__WEBPACK_IMPORTED_MODULE_2__["MunicipalityService"]), _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](src_app_services_user_service__WEBPACK_IMPORTED_MODULE_3__["UserService"]), _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](src_assets_alert__WEBPACK_IMPORTED_MODULE_4__["Alert"]), _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](_auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_5__["AuthService"])); };
UpdateUserComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineComponent"]({ type: UpdateUserComponent, selectors: [["app-update-user"]], decls: 37, vars: 11, consts: [[1, "centered-content"], [1, "choices-content"], [1, "form-content"], [1, "second-content"], [1, "fields-content"], [1, "navbar-item", "has-dropdown", "is-hoverable"], [1, "navbar-link"], [1, "navbar-dropdown"], ["class", "navbar-item", 3, "click", 4, "ngFor", "ngForOf"], [1, "element"], ["type", "text", "placeholder", "Ange f\u00F6rnamn:", 1, "input-field", 3, "value"], ["firstName", ""], ["type", "text", "placeholder", "Ange efternamn:", 1, "input-field", 3, "value"], ["lastName", ""], ["type", "email", "placeholder", "Ange email:", 1, "input-field", 3, "value"], ["email", ""], ["type", "password", "placeholder", "Ange l\u00F6senord:", 1, "input-field", 3, "value"], ["password", ""], [1, "other-content"], [1, "permissions-label"], ["title", "Admin", "type", "checkbox", "value", "admin", "name", "admin", "id", "admin", 1, "permission-checkbox", 3, "checked", "checkedChange", "change"], ["admin", ""], [1, "admin-label"], ["name", "schools", 1, "school-dropdown", 3, "placeholder", "data", "ngModel", "settings", "ngModelChange"], ["schools", ""], [1, "button-content"], [1, "create-button", 3, "click"], [1, "navbar-item", 3, "click"]], template: function UpdateUserComponent_Template(rf, ctx) { if (rf & 1) {
        const _r10 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵgetCurrentView"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](0, "app-user-header");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](1, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](2, "div", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](3, "form", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](4, "div", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](5, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](6, "div", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](7, "a", 6);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](8);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](9, "div", 7);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtemplate"](10, UpdateUserComponent_a_10_Template, 3, 1, "a", 8);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](11, "div", 9);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](12, "input", 10, 11);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](14, "div", 9);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](15, "input", 12, 13);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](17, "div", 9);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](18, "input", 14, 15);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](20, "div", 9);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](21, "input", 16, 17);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](23, "div", 18);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](24, "div", 9);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](25, "label", 19);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](26, "Beh\u00F6righeter: ");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](27, "input", 20, 21);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵlistener"]("checkedChange", function UpdateUserComponent_Template_input_checkedChange_27_listener($event) { return ctx.checkAdmin = $event; })("change", function UpdateUserComponent_Template_input_change_27_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵrestoreView"](_r10); const _r5 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](28); return ctx.clickAdmin(_r5.checked); });
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](29, "label", 22);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](30, "Admin");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](31, "div", 9);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](32, "ng-multiselect-dropdown", 23, 24);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵlistener"]("ngModelChange", function UpdateUserComponent_Template_ng_multiselect_dropdown_ngModelChange_32_listener($event) { return ctx.selectedSchools = $event; });
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](34, "div", 25);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](35, "button", 26);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵlistener"]("click", function UpdateUserComponent_Template_button_click_35_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵrestoreView"](_r10); const _r1 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](13); const _r2 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](16); const _r3 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](19); const _r4 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](22); const _r5 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](28); const _r6 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵreference"](33); return ctx.updateUser(_r1.value, _r2.value, _r3.value, _r4.value, _r5.checked, _r6.selectedItems); });
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](36, " Uppdatera anv\u00E4ndare ");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](8);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtextInterpolate1"](" ", ctx.userToUpdateTitle, " ");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("ngForOf", ctx.users);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("value", ctx.userToUpdate.firstName);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](3);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("value", ctx.userToUpdate.lastName);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](3);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("value", ctx.userToUpdate.email);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](3);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("value", ctx.userToUpdate.password);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](6);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("checked", ctx.checkAdmin);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](5);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("placeholder", "V\u00E4lj skolor att tilldela")("data", ctx.schoolsToChoose)("ngModel", ctx.selectedSchools)("settings", ctx.dropdownSettings);
    } }, directives: [_user_header_user_header_component__WEBPACK_IMPORTED_MODULE_6__["UserHeaderComponent"], _angular_forms__WEBPACK_IMPORTED_MODULE_7__["ɵangular_packages_forms_forms_ba"], _angular_forms__WEBPACK_IMPORTED_MODULE_7__["NgControlStatusGroup"], _angular_forms__WEBPACK_IMPORTED_MODULE_7__["NgForm"], _angular_common__WEBPACK_IMPORTED_MODULE_8__["NgForOf"], ng_multiselect_dropdown__WEBPACK_IMPORTED_MODULE_9__["MultiSelectComponent"], _angular_forms__WEBPACK_IMPORTED_MODULE_7__["NgControlStatus"], _angular_forms__WEBPACK_IMPORTED_MODULE_7__["NgModel"]], styles: [".centered-content[_ngcontent-%COMP%] {\n  height: 620px;\n}\n\n.choices-content[_ngcontent-%COMP%] {\n  max-height: 250px;\n  min-height: 250px;\n  justify-content: left;\n}\n\n.form-content[_ngcontent-%COMP%] {\n  display: flex;\n}\n\n.fields-content[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: column;\n  margin-top: 20px;\n}\n\n.other-content[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: column;\n  margin-top: 20px;\n  margin-left: 50px;\n}\n\n.input-field[_ngcontent-%COMP%] {\n  background: #08496d;\n  border: 0px;\n  border-radius: 8px;\n  color: white;\n  width: 250px;\n  height: 18px;\n  margin: 10px;\n  padding: 8px;\n}\n\n.navbar-item[_ngcontent-%COMP%] {\n  cursor: pointer;\n  max-width: 250px;\n  margin-bottom: 10px;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%] {\n  color: white;\n  background: transparent;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%]:hover {\n  color: #1775b8;\n}\n\n.navbar-dropdown[_ngcontent-%COMP%] {\n  height: 210px;\n}\n\n.permissions-label[_ngcontent-%COMP%] {\n  color: white;\n  height: 20px;\n  margin-left: 12px;\n}\n\n.permission-checkbox[_ngcontent-%COMP%] {\n  margin-left: 20px;\n}\n\n.admin-label[_ngcontent-%COMP%] {\n  color: white;\n  height: 20px;\n  margin-left: 5px;\n}\n\n.create-button[_ngcontent-%COMP%] {\n  width: 150px;\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  margin-top: 95px;\n}\n\n.create-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.button-content[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: row;\n  align-items: center;\n  margin-left: 100px;\n}\n\n.school-dropdown[_ngcontent-%COMP%] {\n  color: white;\n  cursor: pointer;\n}\n\n.multiselect-parent[_ngcontent-%COMP%] {\n  width: 300px;\n}\n\n.multiselect-parent[_ngcontent-%COMP%]   .dropdown-toggle[_ngcontent-%COMP%] {\n  width: 300px;\n}\n\n.multiselect-parent[_ngcontent-%COMP%]   .dropdown-menu[_ngcontent-%COMP%] {\n  width: 300px;\n}\n\n.choices-content[_ngcontent-%COMP%] {\n  max-height: 280px;\n  min-height: 280px;\n  justify-content: left;\n}\n\n.second-content[_ngcontent-%COMP%] {\n  display: flex;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uLy4uL2NyZWF0ZS11c2VyL2NyZWF0ZS11c2VyLmNvbXBvbmVudC5zY3NzIiwiLi4vLi4vLi4vLi4vLi4vLi4vdXBkYXRlLXVzZXIuY29tcG9uZW50LnNjc3MiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUE7RUFDRSxhQUFBO0FDQ0Y7O0FERUE7RUFDRSxpQkFBQTtFQUNBLGlCQUFBO0VBQ0EscUJBQUE7QUNDRjs7QURFQTtFQUNFLGFBQUE7QUNDRjs7QURFQTtFQUNFLGFBQUE7RUFDQSxzQkFBQTtFQUNBLGdCQUFBO0FDQ0Y7O0FERUE7RUFDRSxhQUFBO0VBQ0Esc0JBQUE7RUFDQSxnQkFBQTtFQUNBLGlCQUFBO0FDQ0Y7O0FERUE7RUFDRSxtQkFBQTtFQUNBLFdBQUE7RUFDQSxrQkFBQTtFQUNBLFlBQUE7RUFDQSxZQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7RUFDQSxZQUFBO0FDQ0Y7O0FERUE7RUFDRSxlQUFBO0VBUUEsZ0JBQUE7RUFDQSxtQkFBQTtBQ05GOztBREZFO0VBQ0ksWUFBQTtFQUNBLHVCQUFBO0FDSU47O0FESE07RUFDSSxjQUFBO0FDS1Y7O0FERUE7RUFDRSxhQUFBO0FDQ0Y7O0FERUE7RUFDRSxZQUFBO0VBQ0EsWUFBQTtFQUNBLGlCQUFBO0FDQ0Y7O0FERUE7RUFDRSxpQkFBQTtBQ0NGOztBREVBO0VBQ0UsWUFBQTtFQUNBLFlBQUE7RUFDQSxnQkFBQTtBQ0NGOztBREVBO0VBQ0UsWUFBQTtFQUNBLFdBQUE7RUFDQSx1QkFBQTtFQUNBLFlBQUE7RUFJQSxlQUFBO0VBQ0EsZ0JBQUE7QUNGRjs7QURGRTtFQUNFLDBCQUFBO0FDSUo7O0FERUE7RUFDRSxhQUFBO0VBQ0EsbUJBQUE7RUFDQSxtQkFBQTtFQUNBLGtCQUFBO0FDQ0Y7O0FERUE7RUFDRSxZQUFBO0VBQ0EsZUFBQTtBQ0NGOztBREVBO0VBQ0UsWUFBQTtBQ0NGOztBREVBO0VBQ0UsWUFBQTtBQ0NGOztBREVBO0VBQ0UsWUFBQTtBQ0NGOztBQXZHQTtFQUNFLGlCQUFBO0VBQ0EsaUJBQUE7RUFDQSxxQkFBQTtBQTBHRjs7QUFyR0E7RUFDRSxhQUFBO0FBd0dGIiwiZmlsZSI6InVwZGF0ZS11c2VyLmNvbXBvbmVudC5zY3NzIiwic291cmNlc0NvbnRlbnQiOlsiLmNlbnRlcmVkLWNvbnRlbnQge1xuICBoZWlnaHQ6IDYyMHB4O1xufVxuXG4uY2hvaWNlcy1jb250ZW50IHtcbiAgbWF4LWhlaWdodDogMjUwcHg7XG4gIG1pbi1oZWlnaHQ6IDI1MHB4O1xuICBqdXN0aWZ5LWNvbnRlbnQ6IGxlZnQ7XG59XG5cbi5mb3JtLWNvbnRlbnQge1xuICBkaXNwbGF5OiBmbGV4O1xufVxuXG4uZmllbGRzLWNvbnRlbnQge1xuICBkaXNwbGF5OiBmbGV4O1xuICBmbGV4LWRpcmVjdGlvbjogY29sdW1uO1xuICBtYXJnaW4tdG9wOiAyMHB4O1xufVxuXG4ub3RoZXItY29udGVudCB7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiBjb2x1bW47XG4gIG1hcmdpbi10b3A6IDIwcHg7XG4gIG1hcmdpbi1sZWZ0OiA1MHB4O1xufVxuXG4uaW5wdXQtZmllbGQge1xuICBiYWNrZ3JvdW5kOiAjMDg0OTZkO1xuICBib3JkZXI6IDBweDtcbiAgYm9yZGVyLXJhZGl1czogOHB4O1xuICBjb2xvcjogd2hpdGU7XG4gIHdpZHRoOiAyNTBweDtcbiAgaGVpZ2h0OiAxOHB4O1xuICBtYXJnaW46IDEwcHg7XG4gIHBhZGRpbmc6IDhweDtcbn1cblxuLm5hdmJhci1pdGVtIHtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICAubmF2YmFyLWxpbmsge1xuICAgICAgY29sb3I6IGhzbCgwLCAwJSwgMTAwJSk7XG4gICAgICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgICAgICY6aG92ZXIge1xuICAgICAgICAgIGNvbG9yOiAjMTc3NWI4O1xuICAgICAgfVxuICB9XG4gIG1heC13aWR0aDogMjUwcHg7XG4gIG1hcmdpbi1ib3R0b206IDEwcHg7XG59XG5cbi5uYXZiYXItZHJvcGRvd24ge1xuICBoZWlnaHQ6IDIxMHB4O1xufVxuXG4ucGVybWlzc2lvbnMtbGFiZWwge1xuICBjb2xvcjogd2hpdGU7XG4gIGhlaWdodDogMjBweDtcbiAgbWFyZ2luLWxlZnQ6IDEycHg7XG59XG5cbi5wZXJtaXNzaW9uLWNoZWNrYm94IHtcbiAgbWFyZ2luLWxlZnQ6IDIwcHg7XG59XG5cbi5hZG1pbi1sYWJlbCB7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgaGVpZ2h0OiAyMHB4O1xuICBtYXJnaW4tbGVmdDogNXB4O1xufVxuXG4uY3JlYXRlLWJ1dHRvbiB7XG4gIHdpZHRoOiAxNTBweDtcbiAgYm9yZGVyOiAwcHg7XG4gIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICBjb2xvcjogd2hpdGU7XG4gICY6YWN0aXZlIHtcbiAgICB0cmFuc2Zvcm06IHRyYW5zbGF0ZVkoNHB4KTtcbiAgfVxuICBjdXJzb3I6IHBvaW50ZXI7XG4gIG1hcmdpbi10b3A6IDk1cHg7XG59XG5cbi5idXR0b24tY29udGVudCB7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiByb3c7XG4gIGFsaWduLWl0ZW1zOiBjZW50ZXI7XG4gIG1hcmdpbi1sZWZ0OiAxMDBweDtcbn1cblxuLnNjaG9vbC1kcm9wZG93biB7XG4gIGNvbG9yOndoaXRlO1xuICBjdXJzb3I6IHBvaW50ZXI7XG59XG5cbi5tdWx0aXNlbGVjdC1wYXJlbnQge1xuICB3aWR0aDogMzAwcHg7XG59XG5cbi5tdWx0aXNlbGVjdC1wYXJlbnQgLmRyb3Bkb3duLXRvZ2dsZSB7XG4gIHdpZHRoOiAzMDBweDtcbn1cblxuLm11bHRpc2VsZWN0LXBhcmVudCAuZHJvcGRvd24tbWVudSB7XG4gIHdpZHRoOiAzMDBweDtcbn1cbiIsIkBpbXBvcnQgICcuLi9jcmVhdGUtdXNlci9jcmVhdGUtdXNlci5jb21wb25lbnQuc2Nzcyc7XG5cbi5jaG9pY2VzLWNvbnRlbnQge1xuICBtYXgtaGVpZ2h0OiAyODBweDtcbiAgbWluLWhlaWdodDogMjgwcHg7XG4gIGp1c3RpZnktY29udGVudDogbGVmdDtcbn1cblxuXG5cbi5zZWNvbmQtY29udGVudCB7XG4gIGRpc3BsYXk6IGZsZXg7XG59XG5cbiJdfQ== */"] });


/***/ }),

/***/ "KJrp":
/*!*******************************!*\
  !*** ./src/app/models/day.ts ***!
  \*******************************/
/*! exports provided: Day */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "Day", function() { return Day; });
class Day {
    constructor() {
        this.meals = new Array();
    }
}


/***/ }),

/***/ "KxZz":
/*!*********************************************!*\
  !*** ./src/app/services/sharing.service.ts ***!
  \*********************************************/
/*! exports provided: SharingService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "SharingService", function() { return SharingService; });
/* harmony import */ var rxjs__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! rxjs */ "qCKp");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "fXoL");


class SharingService {
    constructor() {
        this.weekObserver = new rxjs__WEBPACK_IMPORTED_MODULE_0__["Subject"]();
        this.menuObserver = new rxjs__WEBPACK_IMPORTED_MODULE_0__["Subject"]();
    }
    setWeek(week) {
        this.weekObserver.next(week);
    }
    getObservableWeek() {
        return this.weekObserver;
    }
    setMenu(menu) {
        this.menuObserver.next(menu);
    }
    getObservableMenu() {
        return this.menuObserver;
    }
}
SharingService.ɵfac = function SharingService_Factory(t) { return new (t || SharingService)(); };
SharingService.ɵprov = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineInjectable"]({ token: SharingService, factory: SharingService.ɵfac, providedIn: 'root' });


/***/ }),

/***/ "LzIz":
/*!******************************************************************************!*\
  !*** ./src/app/pages/admin/admin-menus/delete-menu/delete-menu.component.ts ***!
  \******************************************************************************/
/*! exports provided: DeleteMenuComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "DeleteMenuComponent", function() { return DeleteMenuComponent; });
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _services_menu_service__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./../../../../services/menu.service */ "Gi7S");
/* harmony import */ var src_assets_alert__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! src/assets/alert */ "X440");
/* harmony import */ var src_app_services_municipality_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! src/app/services/municipality.service */ "ZLEw");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @angular/forms */ "3Pt+");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! @angular/common */ "ofXK");






function DeleteMenuComponent_a_6_Template(rf, ctx) { if (rf & 1) {
    const _r3 = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](0, "a", 8);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵlistener"]("click", function DeleteMenuComponent_a_6_Template_a_click_0_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵrestoreView"](_r3); const menu_r1 = ctx.$implicit; const ctx_r2 = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵnextContext"](); return ctx_r2.updateDeleteMenuTitle(menu_r1); });
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](1, "p");
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
} if (rf & 2) {
    const menu_r1 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵadvance"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtextInterpolate"](menu_r1.menuName);
} }
class DeleteMenuComponent {
    constructor(menuService, alert, municipalityService) {
        this.menuService = menuService;
        this.alert = alert;
        this.municipalityService = municipalityService;
        this.menuToDeleteId = '';
        this.subscriptions = [];
    }
    ngOnInit() {
        this.deleteMenuTitle = "Välj matsedel att ta bort: ";
        this.subscriptions.push(this.municipalityService.getSchools().subscribe((schools) => {
            this.schools = schools;
        }, (err) => this.alert.showAlert('Error', 'Skolor kunde inte hämtas från databasen', 'error')));
        this.subscriptions.push(this.municipalityService.getMunicipalities().subscribe((municipalities) => {
            this.municipalities = municipalities;
        }, (err) => this.alert.showAlert('Error', 'Kommuner kunde inte hämtas från databasen', 'error')));
    }
    ngOnDestroy() {
        this.subscriptions.forEach((sub) => {
            sub.unsubscribe();
        });
    }
    updateDeleteMenuTitle(menu) {
        this.deleteMenuTitle = menu.menuName;
        this.menuToDeleteId = menu._id;
    }
    deleteMenu() {
        if (this.menuToDeleteId === '') {
            this.alert.showAlert('', 'Du måste välja en matsedel att ta bort.', 'error');
        }
        else {
            this.alert.showAdvancedAlert('VARNING', 'Vill du ta bort denna matsedel?', 'warning', 'Ja, ta bort', 'Avbryt').then((result) => {
                if (result.isConfirmed) {
                    this.schools.forEach(school => {
                        if (school._menuId === this.menuToDeleteId) {
                            school._menuId = '';
                            let municipalityToUpdate = this.municipalities
                                .filter((municipality) => municipality.schools
                                .some((schoolInMunicipality) => schoolInMunicipality._id === school._id))[0];
                            this.subscriptions.push(this.municipalityService.updateSchool(municipalityToUpdate._id, school).subscribe(() => {
                            }));
                        }
                    });
                    this.subscriptions.push(this.menuService.deleteMenu(this.menuToDeleteId).subscribe(() => {
                    }, (err) => this.alert.showAlert('Error', 'Något gick fel. Menyn kunde inte sparas till användaren', 'error'), () => this.alert.showAlertAndUpdatePage('Borttagen!', 'Matsedeln har blivit borttagen.', 'success')));
                }
            });
        }
    }
}
DeleteMenuComponent.ɵfac = function DeleteMenuComponent_Factory(t) { return new (t || DeleteMenuComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdirectiveInject"](_services_menu_service__WEBPACK_IMPORTED_MODULE_1__["MenuService"]), _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdirectiveInject"](src_assets_alert__WEBPACK_IMPORTED_MODULE_2__["Alert"]), _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdirectiveInject"](src_app_services_municipality_service__WEBPACK_IMPORTED_MODULE_3__["MunicipalityService"])); };
DeleteMenuComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineComponent"]({ type: DeleteMenuComponent, selectors: [["app-delete-menu"]], inputs: { $menus: "$menus" }, decls: 11, vars: 4, consts: [[1, "choices-content"], [1, "add-content"], [1, "navbar-item", "has-dropdown", "is-hoverable"], [1, "navbar-link"], [1, "navbar-dropdown"], ["class", "navbar-item", 3, "click", 4, "ngFor", "ngForOf"], [1, "button-test"], [1, "manage-menu-button-delete", 3, "click"], [1, "navbar-item", 3, "click"]], template: function DeleteMenuComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](0, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](1, "form", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](2, "div", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](3, "a", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](4);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](5, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtemplate"](6, DeleteMenuComponent_a_6_Template, 3, 1, "a", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵpipe"](7, "async");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](8, "div", 6);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](9, "button", 7);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵlistener"]("click", function DeleteMenuComponent_Template_button_click_9_listener() { return ctx.deleteMenu(); });
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](10, " Ta bort matsedel ");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵadvance"](4);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtextInterpolate1"](" ", ctx.deleteMenuTitle, " ");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵproperty"]("ngForOf", _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵpipeBind1"](7, 2, ctx.$menus));
    } }, directives: [_angular_forms__WEBPACK_IMPORTED_MODULE_4__["ɵangular_packages_forms_forms_ba"], _angular_forms__WEBPACK_IMPORTED_MODULE_4__["NgControlStatusGroup"], _angular_forms__WEBPACK_IMPORTED_MODULE_4__["NgForm"], _angular_common__WEBPACK_IMPORTED_MODULE_5__["NgForOf"]], pipes: [_angular_common__WEBPACK_IMPORTED_MODULE_5__["AsyncPipe"]], styles: [".choices-content[_ngcontent-%COMP%] {\n  vertical-align: middle;\n  align-items: center;\n  flex-direction: row;\n}\n\n.input-field[_ngcontent-%COMP%] {\n  background: #08496d;\n  border: 0px;\n  border-radius: 8px;\n  color: white;\n  width: 250px;\n  height: 18px;\n  margin: 10px;\n  padding: 8px;\n}\n\n.manage-menu-button[_ngcontent-%COMP%] {\n  width: 190px;\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  margin-left: 100px;\n}\n\n.manage-menu-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.date-input[_ngcontent-%COMP%] {\n  background: #08496d;\n  border: 0px;\n  border-radius: 8px;\n  color: white;\n  width: 150px;\n  height: 20px;\n  padding: 10px;\n  margin-right: 15px;\n}\n\n.date-label[_ngcontent-%COMP%] {\n  color: white;\n  height: 20px;\n  margin-right: 5px;\n}\n\n.navbar-item[_ngcontent-%COMP%] {\n  margin-top: 3px;\n  cursor: pointer;\n  max-width: 250px;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%] {\n  color: white;\n  background: transparent;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%]:hover {\n  color: #1775b8;\n}\n\n.navbar-dropdown[_ngcontent-%COMP%] {\n  height: 210px;\n}\n\n.add-content[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: row;\n  width: 100%;\n  height: 100%;\n}\n\n.dates[_ngcontent-%COMP%] {\n  display: flex;\n  padding: 5px;\n}\n\n.test[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: column;\n}\n\n.button-test[_ngcontent-%COMP%] {\n  display: flex;\n  justify-content: right;\n}\n\n.manage-menu-button-delete[_ngcontent-%COMP%] {\n  width: 190px;\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  margin-left: 390px;\n}\n\n.manage-menu-button-delete[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uLy4uL2FkbWluLW1lbnVzLmNvbXBvbmVudC5zY3NzIiwiLi4vLi4vLi4vLi4vLi4vLi4vZGVsZXRlLW1lbnUuY29tcG9uZW50LnNjc3MiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUE7RUFDRSxzQkFBQTtFQUNBLG1CQUFBO0VBQ0EsbUJBQUE7QUNDRjs7QURFQTtFQUNFLG1CQUFBO0VBQ0EsV0FBQTtFQUNBLGtCQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7RUFDQSxZQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7QUNDRjs7QURFQTtFQUNFLFlBQUE7RUFDQSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxZQUFBO0VBSUEsZUFBQTtFQUNBLGtCQUFBO0FDRkY7O0FERkU7RUFDRSwwQkFBQTtBQ0lKOztBREVBO0VBQ0UsbUJBQUE7RUFDQSxXQUFBO0VBQ0Esa0JBQUE7RUFDQSxZQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7RUFDQSxhQUFBO0VBQ0Esa0JBQUE7QUNDRjs7QURFQTtFQUNFLFlBQUE7RUFDQSxZQUFBO0VBQ0EsaUJBQUE7QUNDRjs7QURFQTtFQUNFLGVBQUE7RUFDQSxlQUFBO0VBUUEsZ0JBQUE7QUNORjs7QURERTtFQUNJLFlBQUE7RUFDQSx1QkFBQTtBQ0dOOztBREZNO0VBQ0ksY0FBQTtBQ0lWOztBREVBO0VBQ0UsYUFBQTtBQ0NGOztBREVBO0VBQ0UsYUFBQTtFQUNBLG1CQUFBO0VBQ0EsV0FBQTtFQUNBLFlBQUE7QUNDRjs7QURFQTtFQUNFLGFBQUE7RUFDQSxZQUFBO0FDQ0Y7O0FERUE7RUFDRSxhQUFBO0VBQ0Esc0JBQUE7QUNDRjs7QURFQTtFQUNFLGFBQUE7RUFDQSxzQkFBQTtBQ0NGOztBREVBO0VBQ0csWUFBQTtFQUNBLFdBQUE7RUFDQSx1QkFBQTtFQUNBLFlBQUE7RUFJQSxlQUFBO0VBQ0Esa0JBQUE7QUNGSDs7QURGRztFQUNFLDBCQUFBO0FDSUwiLCJmaWxlIjoiZGVsZXRlLW1lbnUuY29tcG9uZW50LnNjc3MiLCJzb3VyY2VzQ29udGVudCI6WyIuY2hvaWNlcy1jb250ZW50IHtcbiAgdmVydGljYWwtYWxpZ246IG1pZGRsZTtcbiAgYWxpZ24taXRlbXM6IGNlbnRlcjtcbiAgZmxleC1kaXJlY3Rpb246IHJvdztcbn1cblxuLmlucHV0LWZpZWxkIHtcbiAgYmFja2dyb3VuZDogIzA4NDk2ZDtcbiAgYm9yZGVyOiAwcHg7XG4gIGJvcmRlci1yYWRpdXM6IDhweDtcbiAgY29sb3I6IHdoaXRlO1xuICB3aWR0aDogMjUwcHg7XG4gIGhlaWdodDogMThweDtcbiAgbWFyZ2luOiAxMHB4O1xuICBwYWRkaW5nOiA4cHg7XG59XG5cbi5tYW5hZ2UtbWVudS1idXR0b24ge1xuICB3aWR0aDogMTkwcHg7XG4gIGJvcmRlcjogMHB4O1xuICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgY29sb3I6IHdoaXRlO1xuICAmOmFjdGl2ZSB7XG4gICAgdHJhbnNmb3JtOiB0cmFuc2xhdGVZKDRweCk7XG4gIH1cbiAgY3Vyc29yOiBwb2ludGVyO1xuICBtYXJnaW4tbGVmdDogMTAwcHg7XG59XG5cbi5kYXRlLWlucHV0IHtcbiAgYmFja2dyb3VuZDogIzA4NDk2ZDtcbiAgYm9yZGVyOiAwcHg7XG4gIGJvcmRlci1yYWRpdXM6IDhweDtcbiAgY29sb3I6IHdoaXRlO1xuICB3aWR0aDogMTUwcHg7XG4gIGhlaWdodDogMjBweDtcbiAgcGFkZGluZzogMTBweDtcbiAgbWFyZ2luLXJpZ2h0OiAxNXB4O1xufVxuXG4uZGF0ZS1sYWJlbHtcbiAgY29sb3I6IHdoaXRlO1xuICBoZWlnaHQ6IDIwcHg7XG4gIG1hcmdpbi1yaWdodDogNXB4O1xufVxuXG4ubmF2YmFyLWl0ZW0ge1xuICBtYXJnaW4tdG9wOiAzcHg7XG4gIGN1cnNvcjogcG9pbnRlcjtcbiAgLm5hdmJhci1saW5rIHtcbiAgICAgIGNvbG9yOiBoc2woMCwgMCUsIDEwMCUpO1xuICAgICAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gICAgICAmOmhvdmVyIHtcbiAgICAgICAgICBjb2xvcjogIzE3NzViODtcbiAgICAgIH1cbiAgfVxuICBtYXgtd2lkdGg6IDI1MHB4O1xufVxuXG4ubmF2YmFyLWRyb3Bkb3duIHtcbiAgaGVpZ2h0OiAyMTBweDtcbn1cblxuLmFkZC1jb250ZW50IHtcbiAgZGlzcGxheTogZmxleDtcbiAgZmxleC1kaXJlY3Rpb246IHJvdztcbiAgd2lkdGg6IDEwMCU7XG4gIGhlaWdodDogMTAwJTtcbn1cblxuLmRhdGVzIHtcbiAgZGlzcGxheTogZmxleDtcbiAgcGFkZGluZzogNXB4O1xufVxuXG4udGVzdCB7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiBjb2x1bW47XG59XG5cbi5idXR0b24tdGVzdCB7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGp1c3RpZnktY29udGVudDogcmlnaHQ7XG59XG5cbi5tYW5hZ2UtbWVudS1idXR0b24tZGVsZXRlIHtcbiAgIHdpZHRoOiAxOTBweDtcbiAgIGJvcmRlcjogMHB4O1xuICAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gICBjb2xvcjogd2hpdGU7XG4gICAmOmFjdGl2ZSB7XG4gICAgIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xuICAgfVxuICAgY3Vyc29yOiBwb2ludGVyO1xuICAgbWFyZ2luLWxlZnQ6IDM5MHB4O1xufVxuIiwiLmNob2ljZXMtY29udGVudCB7XG4gIHZlcnRpY2FsLWFsaWduOiBtaWRkbGU7XG4gIGFsaWduLWl0ZW1zOiBjZW50ZXI7XG4gIGZsZXgtZGlyZWN0aW9uOiByb3c7XG59XG5cbi5pbnB1dC1maWVsZCB7XG4gIGJhY2tncm91bmQ6ICMwODQ5NmQ7XG4gIGJvcmRlcjogMHB4O1xuICBib3JkZXItcmFkaXVzOiA4cHg7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgd2lkdGg6IDI1MHB4O1xuICBoZWlnaHQ6IDE4cHg7XG4gIG1hcmdpbjogMTBweDtcbiAgcGFkZGluZzogOHB4O1xufVxuXG4ubWFuYWdlLW1lbnUtYnV0dG9uIHtcbiAgd2lkdGg6IDE5MHB4O1xuICBib3JkZXI6IDBweDtcbiAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICBtYXJnaW4tbGVmdDogMTAwcHg7XG59XG4ubWFuYWdlLW1lbnUtYnV0dG9uOmFjdGl2ZSB7XG4gIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xufVxuXG4uZGF0ZS1pbnB1dCB7XG4gIGJhY2tncm91bmQ6ICMwODQ5NmQ7XG4gIGJvcmRlcjogMHB4O1xuICBib3JkZXItcmFkaXVzOiA4cHg7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgd2lkdGg6IDE1MHB4O1xuICBoZWlnaHQ6IDIwcHg7XG4gIHBhZGRpbmc6IDEwcHg7XG4gIG1hcmdpbi1yaWdodDogMTVweDtcbn1cblxuLmRhdGUtbGFiZWwge1xuICBjb2xvcjogd2hpdGU7XG4gIGhlaWdodDogMjBweDtcbiAgbWFyZ2luLXJpZ2h0OiA1cHg7XG59XG5cbi5uYXZiYXItaXRlbSB7XG4gIG1hcmdpbi10b3A6IDNweDtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICBtYXgtd2lkdGg6IDI1MHB4O1xufVxuLm5hdmJhci1pdGVtIC5uYXZiYXItbGluayB7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG59XG4ubmF2YmFyLWl0ZW0gLm5hdmJhci1saW5rOmhvdmVyIHtcbiAgY29sb3I6ICMxNzc1Yjg7XG59XG5cbi5uYXZiYXItZHJvcGRvd24ge1xuICBoZWlnaHQ6IDIxMHB4O1xufVxuXG4uYWRkLWNvbnRlbnQge1xuICBkaXNwbGF5OiBmbGV4O1xuICBmbGV4LWRpcmVjdGlvbjogcm93O1xuICB3aWR0aDogMTAwJTtcbiAgaGVpZ2h0OiAxMDAlO1xufVxuXG4uZGF0ZXMge1xuICBkaXNwbGF5OiBmbGV4O1xuICBwYWRkaW5nOiA1cHg7XG59XG5cbi50ZXN0IHtcbiAgZGlzcGxheTogZmxleDtcbiAgZmxleC1kaXJlY3Rpb246IGNvbHVtbjtcbn1cblxuLmJ1dHRvbi10ZXN0IHtcbiAgZGlzcGxheTogZmxleDtcbiAganVzdGlmeS1jb250ZW50OiByaWdodDtcbn1cblxuLm1hbmFnZS1tZW51LWJ1dHRvbi1kZWxldGUge1xuICB3aWR0aDogMTkwcHg7XG4gIGJvcmRlcjogMHB4O1xuICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgY29sb3I6IHdoaXRlO1xuICBjdXJzb3I6IHBvaW50ZXI7XG4gIG1hcmdpbi1sZWZ0OiAzOTBweDtcbn1cbi5tYW5hZ2UtbWVudS1idXR0b24tZGVsZXRlOmFjdGl2ZSB7XG4gIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xufSJdfQ== */"] });


/***/ }),

/***/ "N7ft":
/*!********************************!*\
  !*** ./src/app/models/menu.ts ***!
  \********************************/
/*! exports provided: Menu */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "Menu", function() { return Menu; });
class Menu {
}


/***/ }),

/***/ "RLWv":
/*!**************************************************!*\
  !*** ./src/app/pages/logout/logout.component.ts ***!
  \**************************************************/
/*! exports provided: LogoutComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LogoutComponent", function() { return LogoutComponent; });
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/forms */ "3Pt+");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/router */ "tyNb");



class LogoutComponent {
    constructor() { }
    ngOnInit() {
    }
}
LogoutComponent.ɵfac = function LogoutComponent_Factory(t) { return new (t || LogoutComponent)(); };
LogoutComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineComponent"]({ type: LogoutComponent, selectors: [["app-logout"]], decls: 9, vars: 0, consts: [[1, "centered-content"], [1, "login-content"], [1, "fields"], [1, "logout-label"], [1, "buttons"], ["routerLink", "/", 1, "back-button"]], template: function LogoutComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](0, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](1, "div", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](2, "form");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](3, "div", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](4, "p", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](5, " Du har blivit utloggad! ");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](6, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](7, "button", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](8, " Till startsidan ");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
    } }, directives: [_angular_forms__WEBPACK_IMPORTED_MODULE_1__["ɵangular_packages_forms_forms_ba"], _angular_forms__WEBPACK_IMPORTED_MODULE_1__["NgControlStatusGroup"], _angular_forms__WEBPACK_IMPORTED_MODULE_1__["NgForm"], _angular_router__WEBPACK_IMPORTED_MODULE_2__["RouterLink"]], styles: [".centered-content[_ngcontent-%COMP%] {\n  height: 100%;\n  vertical-align: center;\n}\n\n.login-content[_ngcontent-%COMP%] {\n  background: #063249;\n  display: flex;\n  justify-content: left;\n  align-items: left;\n  max-width: 360px;\n  min-width: 360px;\n  max-height: 160px;\n  min-height: 160px;\n  border-radius: 8px;\n  margin: 3px;\n  padding: 20px;\n}\n\n.fields[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: column;\n}\n\n.back-button[_ngcontent-%COMP%] {\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  padding: 5px;\n  margin-left: 50px;\n  margin-top: 10px;\n}\n\n.back-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.buttons[_ngcontent-%COMP%] {\n  margin-top: 10px;\n  margin-left: 160px;\n}\n\n.logout-label[_ngcontent-%COMP%] {\n  color: white;\n  margin-left: 20px;\n  margin-top: 30px;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uL2xvZ2luLWZhaWxlZC9sb2dpbi1mYWlsZWQuY29tcG9uZW50LnNjc3MiLCIuLi8uLi8uLi8uLi9sb2dvdXQuY29tcG9uZW50LnNjc3MiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUE7RUFDRSxZQUFBO0VBQ0Esc0JBQUE7QUNDRjs7QURFQTtFQUNFLG1CQUFBO0VBQ0EsYUFBQTtFQUNBLHFCQUFBO0VBQ0EsaUJBQUE7RUFDQSxnQkFBQTtFQUNBLGdCQUFBO0VBQ0EsaUJBQUE7RUFDQSxpQkFBQTtFQUNBLGtCQUFBO0VBQ0EsV0FBQTtFQUNBLGFBQUE7QUNDRjs7QURFQTtFQUNFLGFBQUE7RUFDQSxzQkFBQTtBQ0NGOztBREVBO0VBQ0UsV0FBQTtFQUNBLHVCQUFBO0VBQ0EsWUFBQTtFQUlBLGVBQUE7RUFDQSxZQUFBO0VBQ0EsaUJBQUE7RUFDQSxnQkFBQTtBQ0ZGOztBREpFO0VBQ0UsMEJBQUE7QUNNSjs7QURFQTtFQUNFLGdCQUFBO0VBQ0Esa0JBQUE7QUNDRjs7QURFQTtFQUNFLFlBQUE7RUFDQSxpQkFBQTtFQUNBLGdCQUFBO0FDQ0YiLCJmaWxlIjoibG9nb3V0LmNvbXBvbmVudC5zY3NzIiwic291cmNlc0NvbnRlbnQiOlsiLmNlbnRlcmVkLWNvbnRlbnQge1xuICBoZWlnaHQ6IDEwMCU7XG4gIHZlcnRpY2FsLWFsaWduOiBjZW50ZXI7XG59XG5cbi5sb2dpbi1jb250ZW50IHtcbiAgYmFja2dyb3VuZDogIzA2MzI0OTtcbiAgZGlzcGxheTogZmxleDtcbiAganVzdGlmeS1jb250ZW50OiBsZWZ0O1xuICBhbGlnbi1pdGVtczogbGVmdDtcbiAgbWF4LXdpZHRoOiAzNjBweDtcbiAgbWluLXdpZHRoOiAzNjBweDtcbiAgbWF4LWhlaWdodDogMTYwcHg7XG4gIG1pbi1oZWlnaHQ6IDE2MHB4O1xuICBib3JkZXItcmFkaXVzOiA4cHg7XG4gIG1hcmdpbjogM3B4O1xuICBwYWRkaW5nOiAyMHB4O1xufVxuXG4uZmllbGRzIHtcbiAgZGlzcGxheTogZmxleDtcbiAgZmxleC1kaXJlY3Rpb246IGNvbHVtbjtcbn1cblxuLmJhY2stYnV0dG9uIHtcbiAgYm9yZGVyOiAwcHg7XG4gIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICBjb2xvcjogd2hpdGU7XG4gICY6YWN0aXZlIHtcbiAgICB0cmFuc2Zvcm06IHRyYW5zbGF0ZVkoNHB4KTtcbiAgfVxuICBjdXJzb3I6IHBvaW50ZXI7XG4gIHBhZGRpbmc6IDVweDtcbiAgbWFyZ2luLWxlZnQ6IDUwcHg7XG4gIG1hcmdpbi10b3A6IDEwcHg7XG59XG5cbi5idXR0b25zIHtcbiAgbWFyZ2luLXRvcDogMTBweDtcbiAgbWFyZ2luLWxlZnQ6IDE2MHB4O1xufVxuXG4ubG9nb3V0LWxhYmVsIHtcbiAgY29sb3I6IHdoaXRlO1xuICBtYXJnaW4tbGVmdDogMjBweDtcbiAgbWFyZ2luLXRvcDogMzBweDtcbn1cbiIsIi5jZW50ZXJlZC1jb250ZW50IHtcbiAgaGVpZ2h0OiAxMDAlO1xuICB2ZXJ0aWNhbC1hbGlnbjogY2VudGVyO1xufVxuXG4ubG9naW4tY29udGVudCB7XG4gIGJhY2tncm91bmQ6ICMwNjMyNDk7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGp1c3RpZnktY29udGVudDogbGVmdDtcbiAgYWxpZ24taXRlbXM6IGxlZnQ7XG4gIG1heC13aWR0aDogMzYwcHg7XG4gIG1pbi13aWR0aDogMzYwcHg7XG4gIG1heC1oZWlnaHQ6IDE2MHB4O1xuICBtaW4taGVpZ2h0OiAxNjBweDtcbiAgYm9yZGVyLXJhZGl1czogOHB4O1xuICBtYXJnaW46IDNweDtcbiAgcGFkZGluZzogMjBweDtcbn1cblxuLmZpZWxkcyB7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiBjb2x1bW47XG59XG5cbi5iYWNrLWJ1dHRvbiB7XG4gIGJvcmRlcjogMHB4O1xuICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgY29sb3I6IHdoaXRlO1xuICBjdXJzb3I6IHBvaW50ZXI7XG4gIHBhZGRpbmc6IDVweDtcbiAgbWFyZ2luLWxlZnQ6IDUwcHg7XG4gIG1hcmdpbi10b3A6IDEwcHg7XG59XG4uYmFjay1idXR0b246YWN0aXZlIHtcbiAgdHJhbnNmb3JtOiB0cmFuc2xhdGVZKDRweCk7XG59XG5cbi5idXR0b25zIHtcbiAgbWFyZ2luLXRvcDogMTBweDtcbiAgbWFyZ2luLWxlZnQ6IDE2MHB4O1xufVxuXG4ubG9nb3V0LWxhYmVsIHtcbiAgY29sb3I6IHdoaXRlO1xuICBtYXJnaW4tbGVmdDogMjBweDtcbiAgbWFyZ2luLXRvcDogMzBweDtcbn0iXX0= */"] });


/***/ }),

/***/ "Rvib":
/*!****************************************!*\
  !*** ./src/app/models/municipality.ts ***!
  \****************************************/
/*! exports provided: Municipality */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "Municipality", function() { return Municipality; });
class Municipality {
}


/***/ }),

/***/ "Sy1n":
/*!**********************************!*\
  !*** ./src/app/app.component.ts ***!
  \**********************************/
/*! exports provided: AppComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AppComponent", function() { return AppComponent; });
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _services_sharing_service__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./services/sharing.service */ "KxZz");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/router */ "tyNb");
/* harmony import */ var _pages_footer_footer_component__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./pages/footer/footer.component */ "33AS");




class AppComponent {
    constructor(sharingService) {
        this.sharingService = sharingService;
        this.title = 'Matsedeln';
    }
}
AppComponent.ɵfac = function AppComponent_Factory(t) { return new (t || AppComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdirectiveInject"](_services_sharing_service__WEBPACK_IMPORTED_MODULE_1__["SharingService"])); };
AppComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineComponent"]({ type: AppComponent, selectors: [["app-root"]], decls: 3, vars: 0, consts: [[1, "footer-content"]], template: function AppComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelement"](0, "router-outlet");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](1, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelement"](2, "app-footer");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
    } }, directives: [_angular_router__WEBPACK_IMPORTED_MODULE_2__["RouterOutlet"], _pages_footer_footer_component__WEBPACK_IMPORTED_MODULE_3__["FooterComponent"]], styles: [".footer-content[_ngcontent-%COMP%] {\n  position: fixed;\n  bottom: 0px;\n  right: 0px;\n  padding: 20px;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uL2FwcC5jb21wb25lbnQuc2NzcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTtFQUNFLGVBQUE7RUFDQSxXQUFBO0VBQ0EsVUFBQTtFQUNBLGFBQUE7QUFDRiIsImZpbGUiOiJhcHAuY29tcG9uZW50LnNjc3MiLCJzb3VyY2VzQ29udGVudCI6WyIuZm9vdGVyLWNvbnRlbnQge1xuICBwb3NpdGlvbjogZml4ZWQ7XG4gIGJvdHRvbTogMHB4O1xuICByaWdodDogMHB4O1xuICBwYWRkaW5nOiAyMHB4O1xufVxuIl19 */"] });


/***/ }),

/***/ "WPxs":
/*!********************************************************************************!*\
  !*** ./src/app/pages/admin/admin-schools/remove-menu/remove-menu.component.ts ***!
  \********************************************************************************/
/*! exports provided: RemoveMenuComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "RemoveMenuComponent", function() { return RemoveMenuComponent; });
/* harmony import */ var _models_school__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./../../../../models/school */ "ZVnM");
/* harmony import */ var _models_municipality__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./../../../../models/municipality */ "Rvib");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _services_menu_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./../../../../services/menu.service */ "Gi7S");
/* harmony import */ var _services_municipality_service__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./../../../../services/municipality.service */ "ZLEw");
/* harmony import */ var src_assets_alert__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! src/assets/alert */ "X440");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! @angular/forms */ "3Pt+");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! @angular/common */ "ofXK");








function RemoveMenuComponent_a_6_Template(rf, ctx) { if (rf & 1) {
    const _r4 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](0, "a", 8);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("click", function RemoveMenuComponent_a_6_Template_a_click_0_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵrestoreView"](_r4); const municipality_r2 = ctx.$implicit; const ctx_r3 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵnextContext"](); return ctx_r3.chooseMunicipalityToDelete(municipality_r2); });
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](1, "p");
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
} if (rf & 2) {
    const municipality_r2 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtextInterpolate"](municipality_r2.municipalityName);
} }
function RemoveMenuComponent_a_11_Template(rf, ctx) { if (rf & 1) {
    const _r7 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](0, "a", 8);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("click", function RemoveMenuComponent_a_11_Template_a_click_0_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵrestoreView"](_r7); const school_r5 = ctx.$implicit; const ctx_r6 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵnextContext"](); return ctx_r6.chooseSchoolToDelete(school_r5); });
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](1, "p");
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
} if (rf & 2) {
    const school_r5 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtextInterpolate"](school_r5.schoolName);
} }
class RemoveMenuComponent {
    constructor(menuService, municipalityService, alert) {
        this.menuService = menuService;
        this.municipalityService = municipalityService;
        this.alert = alert;
        this.municipalityToDelete = new _models_municipality__WEBPACK_IMPORTED_MODULE_1__["Municipality"]();
        this.schoolToDelete = new _models_school__WEBPACK_IMPORTED_MODULE_0__["School"]();
        this.chosenMunicipalityTitleToDelete = "Välj kommun: ";
        this.chosenSchoolTitleToDelete = "Välj skola: ";
        this.subscriptions = [];
    }
    ngOnInit() { }
    ngOnDestroy() {
        this.subscriptions.forEach((sub) => {
            sub.unsubscribe();
        });
    }
    chooseMunicipalityToDelete(municipality) {
        this.municipalityToDelete = municipality;
        this.chosenMunicipalityTitleToDelete = municipality.municipalityName;
    }
    chooseSchoolToDelete(school) {
        this.schoolToDelete = school;
        this.chosenSchoolTitleToDelete = school.schoolName;
        let sub = this.menuService.getMenuName(school._menuId).subscribe((menuToDelete) => {
            this.menuToDelete = menuToDelete.menuName;
        });
        this.subscriptions.push(sub);
    }
    deleteMenuFromSchool(school) {
        if (this.chosenSchoolTitleToDelete === "Välj skola: ") {
            this.alert.showAlert('', 'Du måste välja en skola att ta bort matsedeln från!', 'error');
        }
        else if (school._menuId === '') {
            this.alert.showAlert('', 'Den valda skolan har ingen matsedel att ta bort!', 'error');
        }
        else {
            this.alert.showAdvancedAlert('VARNING', 'Vill du ta bort denna matsedel från skolan?', 'warning', 'Ja, ta bort', 'Avbryt').then((result) => {
                if (result.isConfirmed) {
                    school._menuId = '';
                    let sub = this.municipalityService.updateSchool(this.municipalityToDelete._id, school).subscribe(() => { }, (err) => this.alert.showAlert('Error', 'Något gick fel. Menyn kunde inte sparas till användaren', 'error'), () => this.alert.showAlertAndUpdatePage('Borttagen!', 'Matsedeln har blivit borttagen från den valda skolan.', 'success'));
                    this.subscriptions.push(sub);
                }
            });
        }
    }
}
RemoveMenuComponent.ɵfac = function RemoveMenuComponent_Factory(t) { return new (t || RemoveMenuComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](_services_menu_service__WEBPACK_IMPORTED_MODULE_3__["MenuService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](_services_municipality_service__WEBPACK_IMPORTED_MODULE_4__["MunicipalityService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](src_assets_alert__WEBPACK_IMPORTED_MODULE_5__["Alert"])); };
RemoveMenuComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdefineComponent"]({ type: RemoveMenuComponent, selectors: [["app-remove-menu"]], inputs: { municipalities: "municipalities" }, decls: 16, vars: 5, consts: [[1, "choices-content"], [1, "add-content"], [1, "navbar-item", "has-dropdown", "is-hoverable"], [1, "navbar-link"], [1, "navbar-dropdown"], ["class", "navbar-item", 3, "click", 4, "ngFor", "ngForOf"], [1, "menuName", 2, "size", "120px"], [1, "manage-menu-button", 3, "click"], [1, "navbar-item", 3, "click"]], template: function RemoveMenuComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](0, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](1, "form", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](2, "div", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](3, "a", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](4);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](5, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtemplate"](6, RemoveMenuComponent_a_6_Template, 3, 1, "a", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](7, "div", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](8, "a", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](9);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](10, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtemplate"](11, RemoveMenuComponent_a_11_Template, 3, 1, "a", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](12, "p", 6);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](13);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](14, "button", 7);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("click", function RemoveMenuComponent_Template_button_click_14_listener() { return ctx.deleteMenuFromSchool(ctx.schoolToDelete); });
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](15, " Ta bort matsedel fr\u00E5n skolan ");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](4);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtextInterpolate1"](" ", ctx.chosenMunicipalityTitleToDelete, " ");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("ngForOf", ctx.municipalities);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](3);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtextInterpolate1"](" ", ctx.chosenSchoolTitleToDelete, " ");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("ngForOf", ctx.municipalityToDelete.schools);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtextInterpolate1"](" ", ctx.menuToDelete, " ");
    } }, directives: [_angular_forms__WEBPACK_IMPORTED_MODULE_6__["ɵangular_packages_forms_forms_ba"], _angular_forms__WEBPACK_IMPORTED_MODULE_6__["NgControlStatusGroup"], _angular_forms__WEBPACK_IMPORTED_MODULE_6__["NgForm"], _angular_common__WEBPACK_IMPORTED_MODULE_7__["NgForOf"]], styles: [".add-content[_ngcontent-%COMP%] {\n  justify-content: center;\n  display: flex;\n  flex-direction: columns;\n}\n\n.manage-menu-button[_ngcontent-%COMP%] {\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  padding: 20px;\n  margin: 30px;\n}\n\n.manage-menu-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.menuName[_ngcontent-%COMP%] {\n  max-width: 200px;\n  color: white;\n  padding: 20px;\n  margin: 30px;\n}\n\n.navbar-item[_ngcontent-%COMP%] {\n  margin-top: 3px;\n  cursor: pointer;\n  max-width: 250px;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%] {\n  color: white;\n  background: transparent;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%]:hover {\n  color: #1775b8;\n}\n\n.navbar-dropdown[_ngcontent-%COMP%] {\n  height: 250px;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uLy4uL2FkbWluLXNjaG9vbHMuY29tcG9uZW50LnNjc3MiLCIuLi8uLi8uLi8uLi8uLi8uLi9yZW1vdmUtbWVudS5jb21wb25lbnQuc2NzcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTtFQUNFLHVCQUFBO0VBQ0EsYUFBQTtFQUNBLHVCQUFBO0FDQ0Y7O0FERUE7RUFDRSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxZQUFBO0VBSUEsZUFBQTtFQUNBLGFBQUE7RUFDQSxZQUFBO0FDRkY7O0FESEU7RUFDRSwwQkFBQTtBQ0tKOztBREVBO0VBQ0UsZ0JBQUE7RUFDQSxZQUFBO0VBQ0EsYUFBQTtFQUNBLFlBQUE7QUNDRjs7QURFQTtFQUNFLGVBQUE7RUFDQSxlQUFBO0VBUUEsZ0JBQUE7QUNORjs7QURERTtFQUNJLFlBQUE7RUFDQSx1QkFBQTtBQ0dOOztBREZNO0VBQ0ksY0FBQTtBQ0lWOztBREVBO0VBQ0UsYUFBQTtBQ0NGIiwiZmlsZSI6InJlbW92ZS1tZW51LmNvbXBvbmVudC5zY3NzIiwic291cmNlc0NvbnRlbnQiOlsiLmFkZC1jb250ZW50IHtcbiAganVzdGlmeS1jb250ZW50OiBjZW50ZXI7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiBjb2x1bW5zO1xufVxuXG4ubWFuYWdlLW1lbnUtYnV0dG9uIHtcbiAgYm9yZGVyOiAwcHg7XG4gIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICBjb2xvcjogd2hpdGU7XG4gICY6YWN0aXZlIHtcbiAgICB0cmFuc2Zvcm06IHRyYW5zbGF0ZVkoNHB4KTtcbiAgfVxuICBjdXJzb3I6IHBvaW50ZXI7XG4gIHBhZGRpbmc6IDIwcHg7XG4gIG1hcmdpbjogMzBweDtcbn1cblxuLm1lbnVOYW1lIHtcbiAgbWF4LXdpZHRoOiAyMDBweDtcbiAgY29sb3I6IHdoaXRlO1xuICBwYWRkaW5nOiAyMHB4O1xuICBtYXJnaW46IDMwcHg7XG59XG5cbi5uYXZiYXItaXRlbSB7XG4gIG1hcmdpbi10b3A6IDNweDtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICAubmF2YmFyLWxpbmsge1xuICAgICAgY29sb3I6IGhzbCgwLCAwJSwgMTAwJSk7XG4gICAgICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgICAgICY6aG92ZXIge1xuICAgICAgICAgIGNvbG9yOiAjMTc3NWI4O1xuICAgICAgfVxuICB9XG4gIG1heC13aWR0aDogMjUwcHg7XG59XG5cbi5uYXZiYXItZHJvcGRvd24ge1xuICBoZWlnaHQ6IDI1MHB4O1xufVxuIiwiLmFkZC1jb250ZW50IHtcbiAganVzdGlmeS1jb250ZW50OiBjZW50ZXI7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiBjb2x1bW5zO1xufVxuXG4ubWFuYWdlLW1lbnUtYnV0dG9uIHtcbiAgYm9yZGVyOiAwcHg7XG4gIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICBjb2xvcjogd2hpdGU7XG4gIGN1cnNvcjogcG9pbnRlcjtcbiAgcGFkZGluZzogMjBweDtcbiAgbWFyZ2luOiAzMHB4O1xufVxuLm1hbmFnZS1tZW51LWJ1dHRvbjphY3RpdmUge1xuICB0cmFuc2Zvcm06IHRyYW5zbGF0ZVkoNHB4KTtcbn1cblxuLm1lbnVOYW1lIHtcbiAgbWF4LXdpZHRoOiAyMDBweDtcbiAgY29sb3I6IHdoaXRlO1xuICBwYWRkaW5nOiAyMHB4O1xuICBtYXJnaW46IDMwcHg7XG59XG5cbi5uYXZiYXItaXRlbSB7XG4gIG1hcmdpbi10b3A6IDNweDtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICBtYXgtd2lkdGg6IDI1MHB4O1xufVxuLm5hdmJhci1pdGVtIC5uYXZiYXItbGluayB7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG59XG4ubmF2YmFyLWl0ZW0gLm5hdmJhci1saW5rOmhvdmVyIHtcbiAgY29sb3I6ICMxNzc1Yjg7XG59XG5cbi5uYXZiYXItZHJvcGRvd24ge1xuICBoZWlnaHQ6IDI1MHB4O1xufSJdfQ== */"] });


/***/ }),

/***/ "X440":
/*!*****************************!*\
  !*** ./src/assets/alert.ts ***!
  \*****************************/
/*! exports provided: Alert */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "Alert", function() { return Alert; });
/* harmony import */ var sweetalert2__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! sweetalert2 */ "PSD3");
/* harmony import */ var sweetalert2__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(sweetalert2__WEBPACK_IMPORTED_MODULE_0__);

class Alert {
    showAlert(title, text, icon) {
        sweetalert2__WEBPACK_IMPORTED_MODULE_0___default.a.fire({
            title: title,
            text: text,
            icon: icon,
            confirmButtonColor: '#063752'
        });
    }
    showAdvancedAlert(title, text, icon, confirmText, cancelText) {
        return sweetalert2__WEBPACK_IMPORTED_MODULE_0___default.a.fire({
            title: title,
            text: text,
            icon: icon,
            showCancelButton: true,
            confirmButtonText: confirmText,
            cancelButtonText: cancelText,
            confirmButtonColor: "#063752",
            cancelButtonColor: "#063752"
        });
    }
    showAlertAndUpdatePage(title, text, icon) {
        sweetalert2__WEBPACK_IMPORTED_MODULE_0___default.a.fire({
            title: title,
            text: text,
            icon: icon,
            confirmButtonColor: '#063752'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.reload();
            }
        });
    }
}


/***/ }),

/***/ "ZAI4":
/*!*******************************!*\
  !*** ./src/app/app.module.ts ***!
  \*******************************/
/*! exports provided: AppModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AppModule", function() { return AppModule; });
/* harmony import */ var _pages_menu_day_day_component__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./pages/menu/day/day.component */ "a0hZ");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _angular_platform_browser__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/platform-browser */ "jhN1");
/* harmony import */ var _app_routing_module__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./app-routing.module */ "vY5A");
/* harmony import */ var _app_component__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./app.component */ "Sy1n");
/* harmony import */ var _pages_header_header_component__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ./pages/header/header.component */ "/QED");
/* harmony import */ var _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! @angular/platform-browser/animations */ "R1ws");
/* harmony import */ var _pages_menu_menu_component__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! ./pages/menu/menu.component */ "B4DE");
/* harmony import */ var _pages_footer_footer_component__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! ./pages/footer/footer.component */ "33AS");
/* harmony import */ var _pages_admin_admin_meals_admin_meals_component__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! ./pages/admin/admin-meals/admin-meals.component */ "z+UM");
/* harmony import */ var _angular_common_locales_sv__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(/*! @angular/common/locales/sv */ "RpMq");
/* harmony import */ var _angular_common_locales_sv__WEBPACK_IMPORTED_MODULE_10___default = /*#__PURE__*/__webpack_require__.n(_angular_common_locales_sv__WEBPACK_IMPORTED_MODULE_10__);
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_11__ = __webpack_require__(/*! @angular/common */ "ofXK");
/* harmony import */ var _pages_admin_admin_schools_admin_schools_component__WEBPACK_IMPORTED_MODULE_12__ = __webpack_require__(/*! ./pages/admin/admin-schools/admin-schools.component */ "xR1h");
/* harmony import */ var _pages_admin_admin_menus_admin_menus_component__WEBPACK_IMPORTED_MODULE_13__ = __webpack_require__(/*! ./pages/admin/admin-menus/admin-menus.component */ "jNtd");
/* harmony import */ var _pages_admin_admin_header_admin_header_component__WEBPACK_IMPORTED_MODULE_14__ = __webpack_require__(/*! ./pages/admin/admin-header/admin-header.component */ "auzb");
/* harmony import */ var _auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_15__ = __webpack_require__(/*! @auth0/auth0-angular */ "2beD");
/* harmony import */ var _angular_common_http__WEBPACK_IMPORTED_MODULE_16__ = __webpack_require__(/*! @angular/common/http */ "tk/3");
/* harmony import */ var _pages_logout_logout_component__WEBPACK_IMPORTED_MODULE_17__ = __webpack_require__(/*! ./pages/logout/logout.component */ "RLWv");
/* harmony import */ var _pages_login_failed_login_failed_component__WEBPACK_IMPORTED_MODULE_18__ = __webpack_require__(/*! ./pages/login-failed/login-failed.component */ "zs3N");
/* harmony import */ var _pages_admin_admin_menus_create_menu_create_menu_component__WEBPACK_IMPORTED_MODULE_19__ = __webpack_require__(/*! ./pages/admin/admin-menus/create-menu/create-menu.component */ "yy7p");
/* harmony import */ var _pages_admin_admin_menus_update_menu_update_menu_component__WEBPACK_IMPORTED_MODULE_20__ = __webpack_require__(/*! ./pages/admin/admin-menus/update-menu/update-menu.component */ "uxnW");
/* harmony import */ var _pages_admin_admin_menus_delete_menu_delete_menu_component__WEBPACK_IMPORTED_MODULE_21__ = __webpack_require__(/*! ./pages/admin/admin-menus/delete-menu/delete-menu.component */ "LzIz");
/* harmony import */ var _pages_admin_admin_schools_add_menu_add_menu_component__WEBPACK_IMPORTED_MODULE_22__ = __webpack_require__(/*! ./pages/admin/admin-schools/add-menu/add-menu.component */ "yQa+");
/* harmony import */ var _pages_admin_admin_schools_remove_menu_remove_menu_component__WEBPACK_IMPORTED_MODULE_23__ = __webpack_require__(/*! ./pages/admin/admin-schools/remove-menu/remove-menu.component */ "WPxs");
/* harmony import */ var src_assets_alert__WEBPACK_IMPORTED_MODULE_24__ = __webpack_require__(/*! src/assets/alert */ "X440");
/* harmony import */ var _pages_admin_admin_user_create_user_create_user_component__WEBPACK_IMPORTED_MODULE_25__ = __webpack_require__(/*! ./pages/admin/admin-user/create-user/create-user.component */ "7exz");
/* harmony import */ var _pages_admin_admin_user_update_user_update_user_component__WEBPACK_IMPORTED_MODULE_26__ = __webpack_require__(/*! ./pages/admin/admin-user/update-user/update-user.component */ "JMhS");
/* harmony import */ var _pages_admin_admin_user_delete_user_delete_user_component__WEBPACK_IMPORTED_MODULE_27__ = __webpack_require__(/*! ./pages/admin/admin-user/delete-user/delete-user.component */ "zSnG");
/* harmony import */ var _pages_admin_admin_user_user_header_user_header_component__WEBPACK_IMPORTED_MODULE_28__ = __webpack_require__(/*! ./pages/admin/admin-user/user-header/user-header.component */ "ySEZ");
/* harmony import */ var ng_multiselect_dropdown__WEBPACK_IMPORTED_MODULE_29__ = __webpack_require__(/*! ng-multiselect-dropdown */ "Egam");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_30__ = __webpack_require__(/*! @angular/forms */ "3Pt+");
/* harmony import */ var _angular_material_select__WEBPACK_IMPORTED_MODULE_31__ = __webpack_require__(/*! @angular/material/select */ "d3UM");
/* harmony import */ var _pages_login_login_component__WEBPACK_IMPORTED_MODULE_32__ = __webpack_require__(/*! ./pages/login/login.component */ "D8EZ");





































Object(_angular_common__WEBPACK_IMPORTED_MODULE_11__["registerLocaleData"])(_angular_common_locales_sv__WEBPACK_IMPORTED_MODULE_10___default.a);
class AppModule {
}
AppModule.ɵfac = function AppModule_Factory(t) { return new (t || AppModule)(); };
AppModule.ɵmod = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineNgModule"]({ type: AppModule, bootstrap: [_app_component__WEBPACK_IMPORTED_MODULE_4__["AppComponent"]] });
AppModule.ɵinj = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineInjector"]({ providers: [{ provide: _angular_common_http__WEBPACK_IMPORTED_MODULE_16__["HTTP_INTERCEPTORS"], useClass: _auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_15__["AuthHttpInterceptor"], multi: true }, { provide: _angular_core__WEBPACK_IMPORTED_MODULE_1__["LOCALE_ID"], useValue: "sv" }, { provide: src_assets_alert__WEBPACK_IMPORTED_MODULE_24__["Alert"] }], imports: [[
            _angular_material_select__WEBPACK_IMPORTED_MODULE_31__["MatSelectModule"],
            _angular_forms__WEBPACK_IMPORTED_MODULE_30__["ReactiveFormsModule"],
            _angular_forms__WEBPACK_IMPORTED_MODULE_30__["FormsModule"],
            ng_multiselect_dropdown__WEBPACK_IMPORTED_MODULE_29__["NgMultiSelectDropDownModule"].forRoot(),
            _angular_platform_browser__WEBPACK_IMPORTED_MODULE_2__["BrowserModule"],
            _app_routing_module__WEBPACK_IMPORTED_MODULE_3__["AppRoutingModule"],
            _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_6__["BrowserAnimationsModule"],
            _angular_common_http__WEBPACK_IMPORTED_MODULE_16__["HttpClientModule"],
            _auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_15__["AuthModule"].forRoot({
                domain: 'dev-urfmpck7.us.auth0.com',
                clientId: 'pt2NL3EhF816ylmCFGLv6kDXIjq1xQI5',
                audience: 'https://dev-urfmpck7.us.auth0.com/api/v2/',
                cacheLocation: "localstorage",
                httpInterceptor: {
                    allowedList: [
                        {
                            // Match any request that starts 'https://dev-fx63i2zd.eu.auth0.com/api/v2/' (note the asterisk)
                            uri: 'https://matsedeln.herokuapp.com/private/*',
                            tokenOptions: {
                                // The attached token should target this audience
                                audience: 'https://dev-urfmpck7.us.auth0.com/api/v2/',
                            }
                        },
                        {
                            // Match any request that starts 'https://dev-fx63i2zd.eu.auth0.com/api/v2/' (note the asterisk)
                            uri: 'http://localhost:4200/private/*',
                            tokenOptions: {
                                // The attached token should target this audience
                                audience: 'https://dev-urfmpck7.us.auth0.com/api/v2/',
                            }
                        },
                        {
                            // Match any request that starts 'https://dev-fx63i2zd.eu.auth0.com/api/v2/' (note the asterisk)
                            uri: 'http://localhost:8080/private/*',
                            tokenOptions: {
                                // The attached token should target this audience
                                audience: 'https://dev-urfmpck7.us.auth0.com/api/v2/',
                            }
                        }
                    ]
                }
            })
        ]] });
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵsetNgModuleScope"](AppModule, { declarations: [_app_component__WEBPACK_IMPORTED_MODULE_4__["AppComponent"],
        _pages_header_header_component__WEBPACK_IMPORTED_MODULE_5__["HeaderComponent"],
        _pages_menu_menu_component__WEBPACK_IMPORTED_MODULE_7__["MenuComponent"],
        _pages_footer_footer_component__WEBPACK_IMPORTED_MODULE_8__["FooterComponent"],
        _pages_admin_admin_meals_admin_meals_component__WEBPACK_IMPORTED_MODULE_9__["AdminMealsComponent"],
        _pages_admin_admin_schools_admin_schools_component__WEBPACK_IMPORTED_MODULE_12__["AdminSchoolsComponent"],
        _pages_admin_admin_menus_admin_menus_component__WEBPACK_IMPORTED_MODULE_13__["AdminMenusComponent"],
        _pages_admin_admin_header_admin_header_component__WEBPACK_IMPORTED_MODULE_14__["AdminHeaderComponent"],
        _pages_logout_logout_component__WEBPACK_IMPORTED_MODULE_17__["LogoutComponent"],
        _pages_login_failed_login_failed_component__WEBPACK_IMPORTED_MODULE_18__["LoginFailedComponent"],
        _pages_menu_day_day_component__WEBPACK_IMPORTED_MODULE_0__["DayComponent"],
        _pages_admin_admin_menus_create_menu_create_menu_component__WEBPACK_IMPORTED_MODULE_19__["CreateMenuComponent"],
        _pages_admin_admin_menus_update_menu_update_menu_component__WEBPACK_IMPORTED_MODULE_20__["UpdateMenuComponent"],
        _pages_admin_admin_menus_delete_menu_delete_menu_component__WEBPACK_IMPORTED_MODULE_21__["DeleteMenuComponent"],
        _pages_admin_admin_schools_add_menu_add_menu_component__WEBPACK_IMPORTED_MODULE_22__["AddMenuComponent"],
        _pages_admin_admin_schools_remove_menu_remove_menu_component__WEBPACK_IMPORTED_MODULE_23__["RemoveMenuComponent"],
        _pages_admin_admin_user_create_user_create_user_component__WEBPACK_IMPORTED_MODULE_25__["CreateUserComponent"],
        _pages_admin_admin_user_update_user_update_user_component__WEBPACK_IMPORTED_MODULE_26__["UpdateUserComponent"],
        _pages_admin_admin_user_delete_user_delete_user_component__WEBPACK_IMPORTED_MODULE_27__["DeleteUserComponent"],
        _pages_admin_admin_user_user_header_user_header_component__WEBPACK_IMPORTED_MODULE_28__["UserHeaderComponent"],
        _pages_login_login_component__WEBPACK_IMPORTED_MODULE_32__["LoginComponent"]], imports: [_angular_material_select__WEBPACK_IMPORTED_MODULE_31__["MatSelectModule"],
        _angular_forms__WEBPACK_IMPORTED_MODULE_30__["ReactiveFormsModule"],
        _angular_forms__WEBPACK_IMPORTED_MODULE_30__["FormsModule"], ng_multiselect_dropdown__WEBPACK_IMPORTED_MODULE_29__["NgMultiSelectDropDownModule"], _angular_platform_browser__WEBPACK_IMPORTED_MODULE_2__["BrowserModule"],
        _app_routing_module__WEBPACK_IMPORTED_MODULE_3__["AppRoutingModule"],
        _angular_platform_browser_animations__WEBPACK_IMPORTED_MODULE_6__["BrowserAnimationsModule"],
        _angular_common_http__WEBPACK_IMPORTED_MODULE_16__["HttpClientModule"], _auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_15__["AuthModule"]] }); })();


/***/ }),

/***/ "ZLEw":
/*!**************************************************!*\
  !*** ./src/app/services/municipality.service.ts ***!
  \**************************************************/
/*! exports provided: MunicipalityService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "MunicipalityService", function() { return MunicipalityService; });
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _web_req_service__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./web-req.service */ "F/c2");


class MunicipalityService {
    constructor(webReqService) {
        this.webReqService = webReqService;
    }
    getMunicipalities() {
        return this.webReqService.get('public/municipality');
    }
    updateSchool(municipalityId, school) {
        return this.webReqService.patch(`private/municipality/${municipalityId}/school/${school._id}`, school);
    }
    getSchools() {
        return this.webReqService.get('public/schools');
    }
}
MunicipalityService.ɵfac = function MunicipalityService_Factory(t) { return new (t || MunicipalityService)(_angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵinject"](_web_req_service__WEBPACK_IMPORTED_MODULE_1__["WebReqService"])); };
MunicipalityService.ɵprov = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineInjectable"]({ token: MunicipalityService, factory: MunicipalityService.ɵfac, providedIn: 'root' });


/***/ }),

/***/ "ZVnM":
/*!**********************************!*\
  !*** ./src/app/models/school.ts ***!
  \**********************************/
/*! exports provided: School */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "School", function() { return School; });
class School {
}


/***/ }),

/***/ "a0hZ":
/*!*************************************************!*\
  !*** ./src/app/pages/menu/day/day.component.ts ***!
  \*************************************************/
/*! exports provided: DayComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "DayComponent", function() { return DayComponent; });
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _angular_platform_browser__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/platform-browser */ "jhN1");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common */ "ofXK");



function DayComponent_div_0_ng_template_12_div_0_Template(rf, ctx) { if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](0, "div", 6);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelement"](1, "div", 7);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
} if (rf & 2) {
    const meal_r3 = ctx.$implicit;
    const ctx_r2 = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵnextContext"](3);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵadvance"](1);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵproperty"]("innerHTML", ctx_r2.sanitizer.bypassSecurityTrustHtml(ctx_r2.getFoodSpecs(meal_r3)), _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵsanitizeHtml"]);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵadvance"](1);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtextInterpolate1"](" ", meal_r3.mealName, " ");
} }
function DayComponent_div_0_ng_template_12_Template(rf, ctx) { if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtemplate"](0, DayComponent_div_0_ng_template_12_div_0_Template, 3, 2, "div", 5);
} if (rf & 2) {
    const ctx_r1 = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵnextContext"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵproperty"]("ngForOf", ctx_r1.day.meals);
} }
const _c0 = function (a0) { return { "today": a0 }; };
function DayComponent_div_0_Template(rf, ctx) { if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](0, "div");
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](1, "div", 1);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](2, "div", 2);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵpipe"](3, "date");
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵpipe"](4, "date");
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](5);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵpipe"](6, "titlecase");
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵpipe"](7, "date");
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelement"](8, "br");
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](9);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵpipe"](10, "date");
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](11, "div", 3);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtemplate"](12, DayComponent_div_0_ng_template_12_Template, 1, 1, "ng-template", 4);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
} if (rf & 2) {
    const ctx_r0 = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵnextContext"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵadvance"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵproperty"]("ngClass", _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵpureFunction1"](17, _c0, _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵpipeBind2"](3, 4, ctx_r0.day.date, "mediumDate") === _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵpipeBind2"](4, 7, ctx_r0.dateToday, "mediumDate")));
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵadvance"](3);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtextInterpolate1"](" ", _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵpipeBind1"](6, 10, _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵpipeBind2"](7, 12, ctx_r0.day.date, "EEEE")), "");
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵadvance"](4);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtextInterpolate1"]("", _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵpipeBind1"](10, 15, ctx_r0.day.date), " ");
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵadvance"](3);
    _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵproperty"]("ngIf", ctx_r0.day.meals);
} }
class DayComponent {
    constructor(sanitizer) {
        this.sanitizer = sanitizer;
        this.dateToday = new Date(Date.now());
    }
    ngOnInit() {
    }
    getFoodSpecs(meal) {
        let returnString = '';
        returnString += '&nbsp&nbsp';
        meal.foodSpecs.forEach(foodSpec => {
            if (foodSpec === 'veg') {
                returnString += `<i class="fas fa-seedling" style='color:darkGreen' title="Vegetarisk"></i>`;
            }
            else if (foodSpec === 'hot') {
                returnString += `<i class="fas fa-pepper-hot" style='color:red' title="Stark"></i>`;
            }
            else if (foodSpec === 'pig') {
                returnString += `<i class="fas fa-bacon" style='color:pink' title="Fläsk"></i>`;
            }
            returnString += '&nbsp&nbsp';
        });
        return returnString;
    }
}
DayComponent.ɵfac = function DayComponent_Factory(t) { return new (t || DayComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdirectiveInject"](_angular_platform_browser__WEBPACK_IMPORTED_MODULE_1__["DomSanitizer"])); };
DayComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineComponent"]({ type: DayComponent, selectors: [["app-day"]], inputs: { day: "day" }, decls: 1, vars: 1, consts: [[4, "ngIf"], [1, "day-content"], [1, "day-date", 3, "ngClass"], [1, "meals-content"], [3, "ngIf"], ["class", "day-meals", 4, "ngFor", "ngForOf"], [1, "day-meals"], [1, "icons", 3, "innerHTML"]], template: function DayComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtemplate"](0, DayComponent_div_0_Template, 13, 19, "div", 0);
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵproperty"]("ngIf", ctx.day);
    } }, directives: [_angular_common__WEBPACK_IMPORTED_MODULE_2__["NgIf"], _angular_common__WEBPACK_IMPORTED_MODULE_2__["NgClass"], _angular_common__WEBPACK_IMPORTED_MODULE_2__["NgForOf"]], pipes: [_angular_common__WEBPACK_IMPORTED_MODULE_2__["DatePipe"], _angular_common__WEBPACK_IMPORTED_MODULE_2__["TitleCasePipe"]], styles: [".today[_ngcontent-%COMP%] {\n  border: solid 1px white;\n}\n\n.day-date[_ngcontent-%COMP%] {\n  background: #08496d;\n  display: flex;\n  max-width: 80px;\n  height: 90%;\n  justify-content: center;\n  align-items: center;\n  padding: 10px;\n  margin: 3px;\n  border-radius: 8px;\n  color: white;\n}\n\n.meals-content[_ngcontent-%COMP%] {\n  display: rows;\n  width: 100%;\n}\n\n.day-meals[_ngcontent-%COMP%] {\n  background: #08496d;\n  display: flex;\n  width: 100%;\n  height: 20%;\n  justify-content: left;\n  align-items: center;\n  margin: 5px;\n  border-radius: 8px;\n  color: white;\n}\n\n.day-content[_ngcontent-%COMP%] {\n  background: #063249;\n  display: flex;\n  justify-content: left;\n  align-items: left;\n  max-width: 800px;\n  min-width: 800px;\n  max-height: 140px;\n  min-height: 140px;\n  border-radius: 8px;\n  margin: 3px;\n  padding: 20px;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uL2RheS5jb21wb25lbnQuc2NzcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTtFQUNFLHVCQUFBO0FBQ0Y7O0FBRUE7RUFDRSxtQkFBQTtFQUNBLGFBQUE7RUFDQSxlQUFBO0VBQ0EsV0FBQTtFQUNBLHVCQUFBO0VBQ0EsbUJBQUE7RUFDQSxhQUFBO0VBQ0EsV0FBQTtFQUNBLGtCQUFBO0VBQ0EsWUFBQTtBQUNGOztBQUVBO0VBQ0UsYUFBQTtFQUNBLFdBQUE7QUFDRjs7QUFFQTtFQUNFLG1CQUFBO0VBQ0EsYUFBQTtFQUNBLFdBQUE7RUFDQSxXQUFBO0VBQ0EscUJBQUE7RUFDQSxtQkFBQTtFQUNBLFdBQUE7RUFDQSxrQkFBQTtFQUNBLFlBQUE7QUFDRjs7QUFFQTtFQUNFLG1CQUFBO0VBQ0EsYUFBQTtFQUNBLHFCQUFBO0VBQ0EsaUJBQUE7RUFDQSxnQkFBQTtFQUNBLGdCQUFBO0VBQ0EsaUJBQUE7RUFDQSxpQkFBQTtFQUNBLGtCQUFBO0VBQ0EsV0FBQTtFQUNBLGFBQUE7QUFDRiIsImZpbGUiOiJkYXkuY29tcG9uZW50LnNjc3MiLCJzb3VyY2VzQ29udGVudCI6WyIudG9kYXkge1xuICBib3JkZXI6IHNvbGlkIDFweCB3aGl0ZTtcbn1cblxuLmRheS1kYXRlIHtcbiAgYmFja2dyb3VuZDogIzA4NDk2ZDtcbiAgZGlzcGxheTogZmxleDtcbiAgbWF4LXdpZHRoOiA4MHB4O1xuICBoZWlnaHQ6IDkwJTtcbiAganVzdGlmeS1jb250ZW50OiBjZW50ZXI7XG4gIGFsaWduLWl0ZW1zOiBjZW50ZXI7XG4gIHBhZGRpbmc6MTBweDtcbiAgbWFyZ2luOjNweDtcbiAgYm9yZGVyLXJhZGl1czogOHB4O1xuICBjb2xvcjogd2hpdGU7XG59XG5cbi5tZWFscy1jb250ZW50IHtcbiAgZGlzcGxheTogcm93cztcbiAgd2lkdGg6IDEwMCU7XG59XG5cbi5kYXktbWVhbHMge1xuICBiYWNrZ3JvdW5kOiAjMDg0OTZkO1xuICBkaXNwbGF5OiBmbGV4O1xuICB3aWR0aDogMTAwJTtcbiAgaGVpZ2h0OiAyMCU7XG4gIGp1c3RpZnktY29udGVudDogbGVmdDtcbiAgYWxpZ24taXRlbXM6IGNlbnRlcjtcbiAgbWFyZ2luOiA1cHg7XG4gIGJvcmRlci1yYWRpdXM6IDhweDtcbiAgY29sb3I6IHdoaXRlO1xufVxuXG4uZGF5LWNvbnRlbnQge1xuICBiYWNrZ3JvdW5kOiAjMDYzMjQ5O1xuICBkaXNwbGF5OiBmbGV4O1xuICBqdXN0aWZ5LWNvbnRlbnQ6IGxlZnQ7XG4gIGFsaWduLWl0ZW1zOiBsZWZ0O1xuICBtYXgtd2lkdGg6IDgwMHB4O1xuICBtaW4td2lkdGg6IDgwMHB4O1xuICBtYXgtaGVpZ2h0OiAxNDBweDtcbiAgbWluLWhlaWdodDogMTQwcHg7XG4gIGJvcmRlci1yYWRpdXM6IDhweDtcbiAgbWFyZ2luOiAzcHg7XG4gIHBhZGRpbmc6IDIwcHg7XG59XG4iXX0= */"] });


/***/ }),

/***/ "auzb":
/*!********************************************************************!*\
  !*** ./src/app/pages/admin/admin-header/admin-header.component.ts ***!
  \********************************************************************/
/*! exports provided: AdminHeaderComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AdminHeaderComponent", function() { return AdminHeaderComponent; });
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/router */ "tyNb");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/common */ "ofXK");



class AdminHeaderComponent {
    constructor(router, location) {
        this.router = router;
        this.location = location;
    }
    ngOnInit() {
    }
}
AdminHeaderComponent.ɵfac = function AdminHeaderComponent_Factory(t) { return new (t || AdminHeaderComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdirectiveInject"](_angular_router__WEBPACK_IMPORTED_MODULE_1__["Router"]), _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdirectiveInject"](_angular_common__WEBPACK_IMPORTED_MODULE_2__["Location"])); };
AdminHeaderComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineComponent"]({ type: AdminHeaderComponent, selectors: [["app-admin-header"]], decls: 8, vars: 0, consts: [[1, "centered-content"], [1, "header-content"], ["routerLink", "/adminMenus", "routerLinkActive", "selected", 1, "menu-button"], ["routerLink", "/adminSchools", "routerLinkActive", "selected", 1, "school-button"], ["routerLink", "/adminMeals", "routerLinkActive", "selected", 1, "meal-button"]], template: function AdminHeaderComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](0, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](1, "div", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](2, "button", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](3, " Administrera matsedel ");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](4, "button", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](5, " Administrera skola ");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](6, "button", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](7, " Administrera matr\u00E4tter ");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
    } }, directives: [_angular_router__WEBPACK_IMPORTED_MODULE_1__["RouterLink"], _angular_router__WEBPACK_IMPORTED_MODULE_1__["RouterLinkActive"]], styles: [".centered-content[_ngcontent-%COMP%] {\n  height: 100%;\n  max-height: 50px;\n  min-height: 50px;\n  padding: 15px;\n}\n\n.header-content[_ngcontent-%COMP%] {\n  display: flex;\n  padding: 15px;\n}\n\n.menu-button[_ngcontent-%COMP%] {\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  padding: 20px;\n  margin-right: 60px;\n}\n\n.menu-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.school-button[_ngcontent-%COMP%] {\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  padding: 20px;\n}\n\n.school-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.meal-button[_ngcontent-%COMP%] {\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  padding: 20px;\n  margin-left: 60px;\n}\n\n.meal-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.selected[_ngcontent-%COMP%] {\n  color: #1775b8;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uL2FkbWluLWhlYWRlci5jb21wb25lbnQuc2NzcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTtFQUNFLFlBQUE7RUFDQSxnQkFBQTtFQUNBLGdCQUFBO0VBQ0EsYUFBQTtBQUNGOztBQUVBO0VBQ0UsYUFBQTtFQUNBLGFBQUE7QUFDRjs7QUFFQTtFQUNFLFdBQUE7RUFDQSx1QkFBQTtFQUNBLFlBQUE7RUFJQSxlQUFBO0VBQ0EsYUFBQTtFQUNBLGtCQUFBO0FBRkY7O0FBSEU7RUFDRSwwQkFBQTtBQUtKOztBQUVBO0VBQ0UsV0FBQTtFQUNBLHVCQUFBO0VBQ0EsWUFBQTtFQUlBLGVBQUE7RUFDQSxhQUFBO0FBRkY7O0FBRkU7RUFDRSwwQkFBQTtBQUlKOztBQUVBO0VBQ0UsV0FBQTtFQUNBLHVCQUFBO0VBQ0EsWUFBQTtFQUlBLGVBQUE7RUFDQSxhQUFBO0VBQ0EsaUJBQUE7QUFGRjs7QUFIRTtFQUNFLDBCQUFBO0FBS0o7O0FBRUE7RUFDRSxjQUFBO0FBQ0YiLCJmaWxlIjoiYWRtaW4taGVhZGVyLmNvbXBvbmVudC5zY3NzIiwic291cmNlc0NvbnRlbnQiOlsiLmNlbnRlcmVkLWNvbnRlbnQge1xuICBoZWlnaHQ6IDEwMCU7XG4gIG1heC1oZWlnaHQ6IDUwcHg7XG4gIG1pbi1oZWlnaHQ6IDUwcHg7XG4gIHBhZGRpbmc6IDE1cHg7XG59XG5cbi5oZWFkZXItY29udGVudCB7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIHBhZGRpbmc6IDE1cHg7XG59XG5cbi5tZW51LWJ1dHRvbiB7XG4gIGJvcmRlcjogMHB4O1xuICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgY29sb3I6IHdoaXRlO1xuICAmOmFjdGl2ZSB7XG4gICAgdHJhbnNmb3JtOiB0cmFuc2xhdGVZKDRweCk7XG4gIH1cbiAgY3Vyc29yOiBwb2ludGVyO1xuICBwYWRkaW5nOiAyMHB4O1xuICBtYXJnaW4tcmlnaHQ6IDYwcHg7XG59XG5cbi5zY2hvb2wtYnV0dG9uIHtcbiAgYm9yZGVyOiAwcHg7XG4gIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICBjb2xvcjogd2hpdGU7XG4gICY6YWN0aXZlIHtcbiAgICB0cmFuc2Zvcm06IHRyYW5zbGF0ZVkoNHB4KTtcbiAgfVxuICBjdXJzb3I6IHBvaW50ZXI7XG4gIHBhZGRpbmc6IDIwcHg7XG59XG5cbi5tZWFsLWJ1dHRvbiB7XG4gIGJvcmRlcjogMHB4O1xuICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgY29sb3I6IHdoaXRlO1xuICAmOmFjdGl2ZSB7XG4gICAgdHJhbnNmb3JtOiB0cmFuc2xhdGVZKDRweCk7XG4gIH1cbiAgY3Vyc29yOiBwb2ludGVyO1xuICBwYWRkaW5nOiAyMHB4O1xuICBtYXJnaW4tbGVmdDogNjBweDtcbn1cblxuLnNlbGVjdGVkIHtcbiAgY29sb3I6ICMxNzc1Yjg7XG59XG4iXX0= */"] });


/***/ }),

/***/ "hi36":
/*!********************************!*\
  !*** ./src/app/models/meal.ts ***!
  \********************************/
/*! exports provided: Meal */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "Meal", function() { return Meal; });
class Meal {
    constructor() {
        this.foodSpecs = new Array();
    }
}


/***/ }),

/***/ "jNtd":
/*!******************************************************************!*\
  !*** ./src/app/pages/admin/admin-menus/admin-menus.component.ts ***!
  \******************************************************************/
/*! exports provided: AdminMenusComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AdminMenusComponent", function() { return AdminMenusComponent; });
/* harmony import */ var rxjs_operators__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! rxjs/operators */ "kU1M");
/* harmony import */ var src_app_models_user__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! src/app/models/user */ "2hxB");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var src_app_services_menu_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! src/app/services/menu.service */ "Gi7S");
/* harmony import */ var _auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @auth0/auth0-angular */ "2beD");
/* harmony import */ var _admin_header_admin_header_component__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ../admin-header/admin-header.component */ "auzb");
/* harmony import */ var _create_menu_create_menu_component__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ./create-menu/create-menu.component */ "yy7p");
/* harmony import */ var _update_menu_update_menu_component__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! ./update-menu/update-menu.component */ "uxnW");
/* harmony import */ var _delete_menu_delete_menu_component__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! ./delete-menu/delete-menu.component */ "LzIz");









class AdminMenusComponent {
    constructor(menuService, auth) {
        this.menuService = menuService;
        this.auth = auth;
        this.subscriptions = [];
    }
    ngOnInit() {
        this.auth.user$.subscribe((user) => {
            let currentUser = new src_app_models_user__WEBPACK_IMPORTED_MODULE_1__["User"]();
            currentUser.setUserFromAuthPic(user.picture);
            if (currentUser.permissions.some(permission => permission === 'admin')) {
                this.$menus = this.menuService.getMenus();
            }
            else {
                this.$menus = this.menuService.getMenus().pipe(Object(rxjs_operators__WEBPACK_IMPORTED_MODULE_0__["map"])((menu) => menu.filter(m => currentUser.menuIds.some(id => id === m._id))));
            }
        });
    }
}
AdminMenusComponent.ɵfac = function AdminMenusComponent_Factory(t) { return new (t || AdminMenusComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](src_app_services_menu_service__WEBPACK_IMPORTED_MODULE_3__["MenuService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](_auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_4__["AuthService"])); };
AdminMenusComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdefineComponent"]({ type: AdminMenusComponent, selectors: [["app-admin-menus"]], decls: 6, vars: 2, consts: [[1, "centered-content"], [1, "admin-content"], [3, "$menus"]], template: function AdminMenusComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelement"](0, "app-admin-header");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](1, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](2, "div", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelement"](3, "app-create-menu");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelement"](4, "app-update-menu", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelement"](5, "app-delete-menu", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](4);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("$menus", ctx.$menus);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](1);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("$menus", ctx.$menus);
    } }, directives: [_admin_header_admin_header_component__WEBPACK_IMPORTED_MODULE_5__["AdminHeaderComponent"], _create_menu_create_menu_component__WEBPACK_IMPORTED_MODULE_6__["CreateMenuComponent"], _update_menu_update_menu_component__WEBPACK_IMPORTED_MODULE_7__["UpdateMenuComponent"], _delete_menu_delete_menu_component__WEBPACK_IMPORTED_MODULE_8__["DeleteMenuComponent"]], styles: [".choices-content[_ngcontent-%COMP%] {\n  vertical-align: middle;\n  align-items: center;\n  flex-direction: row;\n}\n\n.input-field[_ngcontent-%COMP%] {\n  background: #08496d;\n  border: 0px;\n  border-radius: 8px;\n  color: white;\n  width: 250px;\n  height: 18px;\n  margin: 10px;\n  padding: 8px;\n}\n\n.manage-menu-button[_ngcontent-%COMP%] {\n  width: 190px;\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  margin-left: 100px;\n}\n\n.manage-menu-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.date-input[_ngcontent-%COMP%] {\n  background: #08496d;\n  border: 0px;\n  border-radius: 8px;\n  color: white;\n  width: 150px;\n  height: 20px;\n  padding: 10px;\n  margin-right: 15px;\n}\n\n.date-label[_ngcontent-%COMP%] {\n  color: white;\n  height: 20px;\n  margin-right: 5px;\n}\n\n.navbar-item[_ngcontent-%COMP%] {\n  margin-top: 3px;\n  cursor: pointer;\n  max-width: 250px;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%] {\n  color: white;\n  background: transparent;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%]:hover {\n  color: #1775b8;\n}\n\n.navbar-dropdown[_ngcontent-%COMP%] {\n  height: 210px;\n}\n\n.add-content[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: row;\n  width: 100%;\n  height: 100%;\n}\n\n.dates[_ngcontent-%COMP%] {\n  display: flex;\n  padding: 5px;\n}\n\n.test[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: column;\n}\n\n.button-test[_ngcontent-%COMP%] {\n  display: flex;\n  justify-content: right;\n}\n\n.manage-menu-button-delete[_ngcontent-%COMP%] {\n  width: 190px;\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  margin-left: 390px;\n}\n\n.manage-menu-button-delete[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uL2FkbWluLW1lbnVzLmNvbXBvbmVudC5zY3NzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBO0VBQ0Usc0JBQUE7RUFDQSxtQkFBQTtFQUNBLG1CQUFBO0FBQ0Y7O0FBRUE7RUFDRSxtQkFBQTtFQUNBLFdBQUE7RUFDQSxrQkFBQTtFQUNBLFlBQUE7RUFDQSxZQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7RUFDQSxZQUFBO0FBQ0Y7O0FBRUE7RUFDRSxZQUFBO0VBQ0EsV0FBQTtFQUNBLHVCQUFBO0VBQ0EsWUFBQTtFQUlBLGVBQUE7RUFDQSxrQkFBQTtBQUZGOztBQUZFO0VBQ0UsMEJBQUE7QUFJSjs7QUFFQTtFQUNFLG1CQUFBO0VBQ0EsV0FBQTtFQUNBLGtCQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7RUFDQSxZQUFBO0VBQ0EsYUFBQTtFQUNBLGtCQUFBO0FBQ0Y7O0FBRUE7RUFDRSxZQUFBO0VBQ0EsWUFBQTtFQUNBLGlCQUFBO0FBQ0Y7O0FBRUE7RUFDRSxlQUFBO0VBQ0EsZUFBQTtFQVFBLGdCQUFBO0FBTkY7O0FBREU7RUFDSSxZQUFBO0VBQ0EsdUJBQUE7QUFHTjs7QUFGTTtFQUNJLGNBQUE7QUFJVjs7QUFFQTtFQUNFLGFBQUE7QUFDRjs7QUFFQTtFQUNFLGFBQUE7RUFDQSxtQkFBQTtFQUNBLFdBQUE7RUFDQSxZQUFBO0FBQ0Y7O0FBRUE7RUFDRSxhQUFBO0VBQ0EsWUFBQTtBQUNGOztBQUVBO0VBQ0UsYUFBQTtFQUNBLHNCQUFBO0FBQ0Y7O0FBRUE7RUFDRSxhQUFBO0VBQ0Esc0JBQUE7QUFDRjs7QUFFQTtFQUNHLFlBQUE7RUFDQSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxZQUFBO0VBSUEsZUFBQTtFQUNBLGtCQUFBO0FBRkg7O0FBRkc7RUFDRSwwQkFBQTtBQUlMIiwiZmlsZSI6ImFkbWluLW1lbnVzLmNvbXBvbmVudC5zY3NzIiwic291cmNlc0NvbnRlbnQiOlsiLmNob2ljZXMtY29udGVudCB7XG4gIHZlcnRpY2FsLWFsaWduOiBtaWRkbGU7XG4gIGFsaWduLWl0ZW1zOiBjZW50ZXI7XG4gIGZsZXgtZGlyZWN0aW9uOiByb3c7XG59XG5cbi5pbnB1dC1maWVsZCB7XG4gIGJhY2tncm91bmQ6ICMwODQ5NmQ7XG4gIGJvcmRlcjogMHB4O1xuICBib3JkZXItcmFkaXVzOiA4cHg7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgd2lkdGg6IDI1MHB4O1xuICBoZWlnaHQ6IDE4cHg7XG4gIG1hcmdpbjogMTBweDtcbiAgcGFkZGluZzogOHB4O1xufVxuXG4ubWFuYWdlLW1lbnUtYnV0dG9uIHtcbiAgd2lkdGg6IDE5MHB4O1xuICBib3JkZXI6IDBweDtcbiAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgJjphY3RpdmUge1xuICAgIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xuICB9XG4gIGN1cnNvcjogcG9pbnRlcjtcbiAgbWFyZ2luLWxlZnQ6IDEwMHB4O1xufVxuXG4uZGF0ZS1pbnB1dCB7XG4gIGJhY2tncm91bmQ6ICMwODQ5NmQ7XG4gIGJvcmRlcjogMHB4O1xuICBib3JkZXItcmFkaXVzOiA4cHg7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgd2lkdGg6IDE1MHB4O1xuICBoZWlnaHQ6IDIwcHg7XG4gIHBhZGRpbmc6IDEwcHg7XG4gIG1hcmdpbi1yaWdodDogMTVweDtcbn1cblxuLmRhdGUtbGFiZWx7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgaGVpZ2h0OiAyMHB4O1xuICBtYXJnaW4tcmlnaHQ6IDVweDtcbn1cblxuLm5hdmJhci1pdGVtIHtcbiAgbWFyZ2luLXRvcDogM3B4O1xuICBjdXJzb3I6IHBvaW50ZXI7XG4gIC5uYXZiYXItbGluayB7XG4gICAgICBjb2xvcjogaHNsKDAsIDAlLCAxMDAlKTtcbiAgICAgIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICAgICAgJjpob3ZlciB7XG4gICAgICAgICAgY29sb3I6ICMxNzc1Yjg7XG4gICAgICB9XG4gIH1cbiAgbWF4LXdpZHRoOiAyNTBweDtcbn1cblxuLm5hdmJhci1kcm9wZG93biB7XG4gIGhlaWdodDogMjEwcHg7XG59XG5cbi5hZGQtY29udGVudCB7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiByb3c7XG4gIHdpZHRoOiAxMDAlO1xuICBoZWlnaHQ6IDEwMCU7XG59XG5cbi5kYXRlcyB7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIHBhZGRpbmc6IDVweDtcbn1cblxuLnRlc3Qge1xuICBkaXNwbGF5OiBmbGV4O1xuICBmbGV4LWRpcmVjdGlvbjogY29sdW1uO1xufVxuXG4uYnV0dG9uLXRlc3Qge1xuICBkaXNwbGF5OiBmbGV4O1xuICBqdXN0aWZ5LWNvbnRlbnQ6IHJpZ2h0O1xufVxuXG4ubWFuYWdlLW1lbnUtYnV0dG9uLWRlbGV0ZSB7XG4gICB3aWR0aDogMTkwcHg7XG4gICBib3JkZXI6IDBweDtcbiAgIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICAgY29sb3I6IHdoaXRlO1xuICAgJjphY3RpdmUge1xuICAgICB0cmFuc2Zvcm06IHRyYW5zbGF0ZVkoNHB4KTtcbiAgIH1cbiAgIGN1cnNvcjogcG9pbnRlcjtcbiAgIG1hcmdpbi1sZWZ0OiAzOTBweDtcbn1cbiJdfQ== */"] });


/***/ }),

/***/ "qfBg":
/*!******************************************!*\
  !*** ./src/app/services/user.service.ts ***!
  \******************************************/
/*! exports provided: UserService */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "UserService", function() { return UserService; });
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _web_req_service__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./web-req.service */ "F/c2");


class UserService {
    constructor(webReqService) {
        this.webReqService = webReqService;
    }
    getUsers() {
        return this.webReqService.get('private/user');
    }
    postUser(user) {
        return this.webReqService.post(`private/user`, user);
    }
    updateUser(user) {
        return this.webReqService.patch(`private/user/${user._id}`, user);
    }
    deleteUser(userId) {
        return this.webReqService.delete(`private/user/${userId}`);
    }
    loginUser(user) {
        console.log(user);
        return this.webReqService.post(`public/login`, user);
    }
}
UserService.ɵfac = function UserService_Factory(t) { return new (t || UserService)(_angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵinject"](_web_req_service__WEBPACK_IMPORTED_MODULE_1__["WebReqService"])); };
UserService.ɵprov = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineInjectable"]({ token: UserService, factory: UserService.ɵfac, providedIn: 'root' });


/***/ }),

/***/ "uxnW":
/*!******************************************************************************!*\
  !*** ./src/app/pages/admin/admin-menus/update-menu/update-menu.component.ts ***!
  \******************************************************************************/
/*! exports provided: UpdateMenuComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "UpdateMenuComponent", function() { return UpdateMenuComponent; });
/* harmony import */ var _models_menu__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./../../../../models/menu */ "N7ft");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/common */ "ofXK");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _services_menu_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./../../../../services/menu.service */ "Gi7S");
/* harmony import */ var src_assets_alert__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! src/assets/alert */ "X440");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! @angular/forms */ "3Pt+");







function UpdateMenuComponent_a_7_Template(rf, ctx) { if (rf & 1) {
    const _r6 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](0, "a", 17);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("click", function UpdateMenuComponent_a_7_Template_a_click_0_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵrestoreView"](_r6); const menu_r4 = ctx.$implicit; const ctx_r5 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵnextContext"](); return ctx_r5.editMenu(menu_r4); });
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](1, "p");
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
} if (rf & 2) {
    const menu_r4 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtextInterpolate"](menu_r4.menuName);
} }
class UpdateMenuComponent {
    constructor(menuService, alert) {
        this.menuService = menuService;
        this.alert = alert;
        this.menuToEdit = new _models_menu__WEBPACK_IMPORTED_MODULE_0__["Menu"]();
        this.editMenuTitle = "Välj matsedel att redigera: ";
        this.menuNameToEdit = "";
        this.subscriptions = [];
    }
    ngOnInit() {
    }
    ngOnDestroy() {
        this.subscriptions.forEach((sub) => {
            sub.unsubscribe();
        });
    }
    editMenu(menu) {
        this.editMenuTitle = menu.menuName;
        this.menuToEdit = menu;
        this.menuNameToEdit = menu.menuName;
        this.chosenMenuStartDate = menu.startDate;
        this.chosenMenuEndDate = menu.endDate;
    }
    saveEditedMenu(menuName, startDate, endDate) {
        let datePipe = new _angular_common__WEBPACK_IMPORTED_MODULE_1__["DatePipe"]('en-US');
        if (menuName === '') {
            this.alert.showAlert('', 'Du måste välja en matsedel att uppdatera.', 'error');
        }
        else if (menuName.length < 1) {
            this.alert.showAlert('', 'Input för menynamnet är för kort. Testa igen!', 'error');
        }
        else if (menuName.length > 40) {
            this.alert.showAlert('', 'Input för menynamnet är för långt. Testa igen!', 'error');
        }
        else {
            this.menuToEdit.menuName = menuName;
            this.menuToEdit.startDate = startDate;
            this.menuToEdit.endDate = endDate;
            let sub = this.menuService.updateMenu(this.menuToEdit).subscribe(() => {
            }, (err) => this.alert.showAlert('Error', 'Något gick fel. Menyn kunde inte sparas till användaren', 'error'), () => this.alert.showAlertAndUpdatePage('Uppdaterad!', 'Matsedeln har blivit uppdaterad.', 'success'));
            this.subscriptions.push(sub);
        }
    }
}
UpdateMenuComponent.ɵfac = function UpdateMenuComponent_Factory(t) { return new (t || UpdateMenuComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](_services_menu_service__WEBPACK_IMPORTED_MODULE_3__["MenuService"]), _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdirectiveInject"](src_assets_alert__WEBPACK_IMPORTED_MODULE_4__["Alert"])); };
UpdateMenuComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵdefineComponent"]({ type: UpdateMenuComponent, selectors: [["app-update-menu"]], inputs: { $menus: "$menus" }, decls: 25, vars: 13, consts: [[1, "choices-content"], [1, "add-content"], [1, "test"], [1, "navbar-item", "has-dropdown", "is-hoverable"], [1, "navbar-link"], [1, "navbar-dropdown"], ["class", "navbar-item", 3, "click", 4, "ngFor", "ngForOf"], ["type", "text", "placeholder", "Matsedelns namn: ", 1, "input-field", 3, "value"], ["menuNameEdit", ""], [1, "dates"], ["for", "start", 1, "date-label"], ["type", "date", "id", "start", "name", "start-date", "min", "2021-01-01", "max", "2030-12-31", 1, "date-input", 3, "value"], ["chosenStartDate", ""], ["type", "date", "id", "end", "name", "end-date", "min", "2021-01-01", "max", "2030-12-31", 1, "date-input", 3, "value"], ["chosenEndDate", ""], [1, "button-test"], [1, "manage-menu-button", 3, "click"], [1, "navbar-item", 3, "click"]], template: function UpdateMenuComponent_Template(rf, ctx) { if (rf & 1) {
        const _r7 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵgetCurrentView"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](0, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](1, "form", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](2, "div", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](3, "div", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](4, "a", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](6, "div", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtemplate"](7, UpdateMenuComponent_a_7_Template, 3, 1, "a", 6);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵpipe"](8, "async");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelement"](9, "input", 7, 8);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](11, "div", 9);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](12, "label", 10);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](13, "Startdatum: ");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelement"](14, "input", 11, 12);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵpipe"](16, "date");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](17, "label", 10);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](18, "Slutdatum: ");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelement"](19, "input", 13, 14);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵpipe"](21, "date");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](22, "div", 15);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementStart"](23, "button", 16);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵlistener"]("click", function UpdateMenuComponent_Template_button_click_23_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵrestoreView"](_r7); const _r1 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵreference"](10); const _r2 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵreference"](15); const _r3 = _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵreference"](20); return ctx.saveEditedMenu(_r1.value, _r2.value, _r3.value); });
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtext"](24, " Spara uppdaterad matsedel ");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵelementEnd"]();
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵtextInterpolate1"](" ", ctx.editMenuTitle, " ");
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("ngForOf", _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵpipeBind1"](8, 5, ctx.$menus));
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("value", ctx.menuNameToEdit);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("value", _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵpipeBind2"](16, 7, ctx.chosenMenuStartDate, "yyyy-MM-dd"));
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵadvance"](5);
        _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵproperty"]("value", _angular_core__WEBPACK_IMPORTED_MODULE_2__["ɵɵpipeBind2"](21, 10, ctx.chosenMenuEndDate, "yyyy-MM-dd"));
    } }, directives: [_angular_forms__WEBPACK_IMPORTED_MODULE_5__["ɵangular_packages_forms_forms_ba"], _angular_forms__WEBPACK_IMPORTED_MODULE_5__["NgControlStatusGroup"], _angular_forms__WEBPACK_IMPORTED_MODULE_5__["NgForm"], _angular_common__WEBPACK_IMPORTED_MODULE_1__["NgForOf"]], pipes: [_angular_common__WEBPACK_IMPORTED_MODULE_1__["AsyncPipe"], _angular_common__WEBPACK_IMPORTED_MODULE_1__["DatePipe"]], styles: [".choices-content[_ngcontent-%COMP%] {\n  vertical-align: middle;\n  align-items: center;\n  flex-direction: row;\n}\n\n.input-field[_ngcontent-%COMP%] {\n  background: #08496d;\n  border: 0px;\n  border-radius: 8px;\n  color: white;\n  width: 250px;\n  height: 18px;\n  margin: 10px;\n  padding: 8px;\n}\n\n.manage-menu-button[_ngcontent-%COMP%] {\n  width: 190px;\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  margin-left: 100px;\n}\n\n.manage-menu-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.date-input[_ngcontent-%COMP%] {\n  background: #08496d;\n  border: 0px;\n  border-radius: 8px;\n  color: white;\n  width: 150px;\n  height: 20px;\n  padding: 10px;\n  margin-right: 15px;\n}\n\n.date-label[_ngcontent-%COMP%] {\n  color: white;\n  height: 20px;\n  margin-right: 5px;\n}\n\n.navbar-item[_ngcontent-%COMP%] {\n  margin-top: 3px;\n  cursor: pointer;\n  max-width: 250px;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%] {\n  color: white;\n  background: transparent;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%]:hover {\n  color: #1775b8;\n}\n\n.navbar-dropdown[_ngcontent-%COMP%] {\n  height: 210px;\n}\n\n.add-content[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: row;\n  width: 100%;\n  height: 100%;\n}\n\n.dates[_ngcontent-%COMP%] {\n  display: flex;\n  padding: 5px;\n}\n\n.test[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: column;\n}\n\n.button-test[_ngcontent-%COMP%] {\n  display: flex;\n  justify-content: right;\n}\n\n.manage-menu-button-delete[_ngcontent-%COMP%] {\n  width: 190px;\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  margin-left: 390px;\n}\n\n.manage-menu-button-delete[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uLy4uL2FkbWluLW1lbnVzLmNvbXBvbmVudC5zY3NzIiwiLi4vLi4vLi4vLi4vLi4vLi4vdXBkYXRlLW1lbnUuY29tcG9uZW50LnNjc3MiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUE7RUFDRSxzQkFBQTtFQUNBLG1CQUFBO0VBQ0EsbUJBQUE7QUNDRjs7QURFQTtFQUNFLG1CQUFBO0VBQ0EsV0FBQTtFQUNBLGtCQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7RUFDQSxZQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7QUNDRjs7QURFQTtFQUNFLFlBQUE7RUFDQSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxZQUFBO0VBSUEsZUFBQTtFQUNBLGtCQUFBO0FDRkY7O0FERkU7RUFDRSwwQkFBQTtBQ0lKOztBREVBO0VBQ0UsbUJBQUE7RUFDQSxXQUFBO0VBQ0Esa0JBQUE7RUFDQSxZQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7RUFDQSxhQUFBO0VBQ0Esa0JBQUE7QUNDRjs7QURFQTtFQUNFLFlBQUE7RUFDQSxZQUFBO0VBQ0EsaUJBQUE7QUNDRjs7QURFQTtFQUNFLGVBQUE7RUFDQSxlQUFBO0VBUUEsZ0JBQUE7QUNORjs7QURERTtFQUNJLFlBQUE7RUFDQSx1QkFBQTtBQ0dOOztBREZNO0VBQ0ksY0FBQTtBQ0lWOztBREVBO0VBQ0UsYUFBQTtBQ0NGOztBREVBO0VBQ0UsYUFBQTtFQUNBLG1CQUFBO0VBQ0EsV0FBQTtFQUNBLFlBQUE7QUNDRjs7QURFQTtFQUNFLGFBQUE7RUFDQSxZQUFBO0FDQ0Y7O0FERUE7RUFDRSxhQUFBO0VBQ0Esc0JBQUE7QUNDRjs7QURFQTtFQUNFLGFBQUE7RUFDQSxzQkFBQTtBQ0NGOztBREVBO0VBQ0csWUFBQTtFQUNBLFdBQUE7RUFDQSx1QkFBQTtFQUNBLFlBQUE7RUFJQSxlQUFBO0VBQ0Esa0JBQUE7QUNGSDs7QURGRztFQUNFLDBCQUFBO0FDSUwiLCJmaWxlIjoidXBkYXRlLW1lbnUuY29tcG9uZW50LnNjc3MiLCJzb3VyY2VzQ29udGVudCI6WyIuY2hvaWNlcy1jb250ZW50IHtcbiAgdmVydGljYWwtYWxpZ246IG1pZGRsZTtcbiAgYWxpZ24taXRlbXM6IGNlbnRlcjtcbiAgZmxleC1kaXJlY3Rpb246IHJvdztcbn1cblxuLmlucHV0LWZpZWxkIHtcbiAgYmFja2dyb3VuZDogIzA4NDk2ZDtcbiAgYm9yZGVyOiAwcHg7XG4gIGJvcmRlci1yYWRpdXM6IDhweDtcbiAgY29sb3I6IHdoaXRlO1xuICB3aWR0aDogMjUwcHg7XG4gIGhlaWdodDogMThweDtcbiAgbWFyZ2luOiAxMHB4O1xuICBwYWRkaW5nOiA4cHg7XG59XG5cbi5tYW5hZ2UtbWVudS1idXR0b24ge1xuICB3aWR0aDogMTkwcHg7XG4gIGJvcmRlcjogMHB4O1xuICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgY29sb3I6IHdoaXRlO1xuICAmOmFjdGl2ZSB7XG4gICAgdHJhbnNmb3JtOiB0cmFuc2xhdGVZKDRweCk7XG4gIH1cbiAgY3Vyc29yOiBwb2ludGVyO1xuICBtYXJnaW4tbGVmdDogMTAwcHg7XG59XG5cbi5kYXRlLWlucHV0IHtcbiAgYmFja2dyb3VuZDogIzA4NDk2ZDtcbiAgYm9yZGVyOiAwcHg7XG4gIGJvcmRlci1yYWRpdXM6IDhweDtcbiAgY29sb3I6IHdoaXRlO1xuICB3aWR0aDogMTUwcHg7XG4gIGhlaWdodDogMjBweDtcbiAgcGFkZGluZzogMTBweDtcbiAgbWFyZ2luLXJpZ2h0OiAxNXB4O1xufVxuXG4uZGF0ZS1sYWJlbHtcbiAgY29sb3I6IHdoaXRlO1xuICBoZWlnaHQ6IDIwcHg7XG4gIG1hcmdpbi1yaWdodDogNXB4O1xufVxuXG4ubmF2YmFyLWl0ZW0ge1xuICBtYXJnaW4tdG9wOiAzcHg7XG4gIGN1cnNvcjogcG9pbnRlcjtcbiAgLm5hdmJhci1saW5rIHtcbiAgICAgIGNvbG9yOiBoc2woMCwgMCUsIDEwMCUpO1xuICAgICAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gICAgICAmOmhvdmVyIHtcbiAgICAgICAgICBjb2xvcjogIzE3NzViODtcbiAgICAgIH1cbiAgfVxuICBtYXgtd2lkdGg6IDI1MHB4O1xufVxuXG4ubmF2YmFyLWRyb3Bkb3duIHtcbiAgaGVpZ2h0OiAyMTBweDtcbn1cblxuLmFkZC1jb250ZW50IHtcbiAgZGlzcGxheTogZmxleDtcbiAgZmxleC1kaXJlY3Rpb246IHJvdztcbiAgd2lkdGg6IDEwMCU7XG4gIGhlaWdodDogMTAwJTtcbn1cblxuLmRhdGVzIHtcbiAgZGlzcGxheTogZmxleDtcbiAgcGFkZGluZzogNXB4O1xufVxuXG4udGVzdCB7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiBjb2x1bW47XG59XG5cbi5idXR0b24tdGVzdCB7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGp1c3RpZnktY29udGVudDogcmlnaHQ7XG59XG5cbi5tYW5hZ2UtbWVudS1idXR0b24tZGVsZXRlIHtcbiAgIHdpZHRoOiAxOTBweDtcbiAgIGJvcmRlcjogMHB4O1xuICAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gICBjb2xvcjogd2hpdGU7XG4gICAmOmFjdGl2ZSB7XG4gICAgIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xuICAgfVxuICAgY3Vyc29yOiBwb2ludGVyO1xuICAgbWFyZ2luLWxlZnQ6IDM5MHB4O1xufVxuIiwiLmNob2ljZXMtY29udGVudCB7XG4gIHZlcnRpY2FsLWFsaWduOiBtaWRkbGU7XG4gIGFsaWduLWl0ZW1zOiBjZW50ZXI7XG4gIGZsZXgtZGlyZWN0aW9uOiByb3c7XG59XG5cbi5pbnB1dC1maWVsZCB7XG4gIGJhY2tncm91bmQ6ICMwODQ5NmQ7XG4gIGJvcmRlcjogMHB4O1xuICBib3JkZXItcmFkaXVzOiA4cHg7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgd2lkdGg6IDI1MHB4O1xuICBoZWlnaHQ6IDE4cHg7XG4gIG1hcmdpbjogMTBweDtcbiAgcGFkZGluZzogOHB4O1xufVxuXG4ubWFuYWdlLW1lbnUtYnV0dG9uIHtcbiAgd2lkdGg6IDE5MHB4O1xuICBib3JkZXI6IDBweDtcbiAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICBtYXJnaW4tbGVmdDogMTAwcHg7XG59XG4ubWFuYWdlLW1lbnUtYnV0dG9uOmFjdGl2ZSB7XG4gIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xufVxuXG4uZGF0ZS1pbnB1dCB7XG4gIGJhY2tncm91bmQ6ICMwODQ5NmQ7XG4gIGJvcmRlcjogMHB4O1xuICBib3JkZXItcmFkaXVzOiA4cHg7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgd2lkdGg6IDE1MHB4O1xuICBoZWlnaHQ6IDIwcHg7XG4gIHBhZGRpbmc6IDEwcHg7XG4gIG1hcmdpbi1yaWdodDogMTVweDtcbn1cblxuLmRhdGUtbGFiZWwge1xuICBjb2xvcjogd2hpdGU7XG4gIGhlaWdodDogMjBweDtcbiAgbWFyZ2luLXJpZ2h0OiA1cHg7XG59XG5cbi5uYXZiYXItaXRlbSB7XG4gIG1hcmdpbi10b3A6IDNweDtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICBtYXgtd2lkdGg6IDI1MHB4O1xufVxuLm5hdmJhci1pdGVtIC5uYXZiYXItbGluayB7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG59XG4ubmF2YmFyLWl0ZW0gLm5hdmJhci1saW5rOmhvdmVyIHtcbiAgY29sb3I6ICMxNzc1Yjg7XG59XG5cbi5uYXZiYXItZHJvcGRvd24ge1xuICBoZWlnaHQ6IDIxMHB4O1xufVxuXG4uYWRkLWNvbnRlbnQge1xuICBkaXNwbGF5OiBmbGV4O1xuICBmbGV4LWRpcmVjdGlvbjogcm93O1xuICB3aWR0aDogMTAwJTtcbiAgaGVpZ2h0OiAxMDAlO1xufVxuXG4uZGF0ZXMge1xuICBkaXNwbGF5OiBmbGV4O1xuICBwYWRkaW5nOiA1cHg7XG59XG5cbi50ZXN0IHtcbiAgZGlzcGxheTogZmxleDtcbiAgZmxleC1kaXJlY3Rpb246IGNvbHVtbjtcbn1cblxuLmJ1dHRvbi10ZXN0IHtcbiAgZGlzcGxheTogZmxleDtcbiAganVzdGlmeS1jb250ZW50OiByaWdodDtcbn1cblxuLm1hbmFnZS1tZW51LWJ1dHRvbi1kZWxldGUge1xuICB3aWR0aDogMTkwcHg7XG4gIGJvcmRlcjogMHB4O1xuICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgY29sb3I6IHdoaXRlO1xuICBjdXJzb3I6IHBvaW50ZXI7XG4gIG1hcmdpbi1sZWZ0OiAzOTBweDtcbn1cbi5tYW5hZ2UtbWVudS1idXR0b24tZGVsZXRlOmFjdGl2ZSB7XG4gIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xufSJdfQ== */"] });


/***/ }),

/***/ "vY5A":
/*!***************************************!*\
  !*** ./src/app/app-routing.module.ts ***!
  \***************************************/
/*! exports provided: AppRoutingModule */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AppRoutingModule", function() { return AppRoutingModule; });
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/router */ "tyNb");
/* harmony import */ var _pages_admin_admin_header_admin_header_component__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./pages/admin/admin-header/admin-header.component */ "auzb");
/* harmony import */ var _pages_admin_admin_meals_admin_meals_component__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./pages/admin/admin-meals/admin-meals.component */ "z+UM");
/* harmony import */ var _pages_admin_admin_menus_admin_menus_component__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./pages/admin/admin-menus/admin-menus.component */ "jNtd");
/* harmony import */ var _pages_admin_admin_schools_admin_schools_component__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./pages/admin/admin-schools/admin-schools.component */ "xR1h");
/* harmony import */ var _pages_admin_admin_user_create_user_create_user_component__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ./pages/admin/admin-user/create-user/create-user.component */ "7exz");
/* harmony import */ var _pages_admin_admin_user_delete_user_delete_user_component__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ./pages/admin/admin-user/delete-user/delete-user.component */ "zSnG");
/* harmony import */ var _pages_admin_admin_user_update_user_update_user_component__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! ./pages/admin/admin-user/update-user/update-user.component */ "JMhS");
/* harmony import */ var _pages_admin_admin_user_user_header_user_header_component__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! ./pages/admin/admin-user/user-header/user-header.component */ "ySEZ");
/* harmony import */ var _pages_login_failed_login_failed_component__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! ./pages/login-failed/login-failed.component */ "zs3N");
/* harmony import */ var _pages_logout_logout_component__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(/*! ./pages/logout/logout.component */ "RLWv");
/* harmony import */ var _pages_menu_menu_component__WEBPACK_IMPORTED_MODULE_11__ = __webpack_require__(/*! ./pages/menu/menu.component */ "B4DE");
/* harmony import */ var _pages_login_login_component__WEBPACK_IMPORTED_MODULE_12__ = __webpack_require__(/*! ./pages/login/login.component */ "D8EZ");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_13__ = __webpack_require__(/*! @angular/core */ "fXoL");















const routes = [
    { path: '', redirectTo: '/', pathMatch: 'full' },
    { path: '', component: _pages_menu_menu_component__WEBPACK_IMPORTED_MODULE_11__["MenuComponent"] },
    { path: 'adminMeals', component: _pages_admin_admin_meals_admin_meals_component__WEBPACK_IMPORTED_MODULE_2__["AdminMealsComponent"] },
    { path: 'adminSchools', component: _pages_admin_admin_schools_admin_schools_component__WEBPACK_IMPORTED_MODULE_4__["AdminSchoolsComponent"] },
    { path: 'adminMenus', component: _pages_admin_admin_menus_admin_menus_component__WEBPACK_IMPORTED_MODULE_3__["AdminMenusComponent"] },
    { path: 'admin', component: _pages_admin_admin_header_admin_header_component__WEBPACK_IMPORTED_MODULE_1__["AdminHeaderComponent"] },
    { path: 'logout', component: _pages_logout_logout_component__WEBPACK_IMPORTED_MODULE_10__["LogoutComponent"] },
    { path: 'login', component: _pages_login_login_component__WEBPACK_IMPORTED_MODULE_12__["LoginComponent"] },
    { path: 'loginFailed', component: _pages_login_failed_login_failed_component__WEBPACK_IMPORTED_MODULE_9__["LoginFailedComponent"] },
    { path: 'user', component: _pages_admin_admin_user_user_header_user_header_component__WEBPACK_IMPORTED_MODULE_8__["UserHeaderComponent"] },
    { path: 'createUser', component: _pages_admin_admin_user_create_user_create_user_component__WEBPACK_IMPORTED_MODULE_5__["CreateUserComponent"] },
    { path: 'updateUser', component: _pages_admin_admin_user_update_user_update_user_component__WEBPACK_IMPORTED_MODULE_7__["UpdateUserComponent"] },
    { path: 'deleteUser', component: _pages_admin_admin_user_delete_user_delete_user_component__WEBPACK_IMPORTED_MODULE_6__["DeleteUserComponent"] }
];
class AppRoutingModule {
}
AppRoutingModule.ɵfac = function AppRoutingModule_Factory(t) { return new (t || AppRoutingModule)(); };
AppRoutingModule.ɵmod = _angular_core__WEBPACK_IMPORTED_MODULE_13__["ɵɵdefineNgModule"]({ type: AppRoutingModule });
AppRoutingModule.ɵinj = _angular_core__WEBPACK_IMPORTED_MODULE_13__["ɵɵdefineInjector"]({ imports: [[_angular_router__WEBPACK_IMPORTED_MODULE_0__["RouterModule"].forRoot(routes)], _angular_router__WEBPACK_IMPORTED_MODULE_0__["RouterModule"]] });
(function () { (typeof ngJitMode === "undefined" || ngJitMode) && _angular_core__WEBPACK_IMPORTED_MODULE_13__["ɵɵsetNgModuleScope"](AppRoutingModule, { imports: [_angular_router__WEBPACK_IMPORTED_MODULE_0__["RouterModule"]], exports: [_angular_router__WEBPACK_IMPORTED_MODULE_0__["RouterModule"]] }); })();


/***/ }),

/***/ "wUJa":
/*!********************************!*\
  !*** ./src/app/models/week.ts ***!
  \********************************/
/*! exports provided: Week */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "Week", function() { return Week; });
class Week {
    constructor() {
        this.days = new Array();
    }
}


/***/ }),

/***/ "xR1h":
/*!**********************************************************************!*\
  !*** ./src/app/pages/admin/admin-schools/admin-schools.component.ts ***!
  \**********************************************************************/
/*! exports provided: AdminSchoolsComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AdminSchoolsComponent", function() { return AdminSchoolsComponent; });
/* harmony import */ var src_app_models_user__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! src/app/models/user */ "2hxB");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var src_app_services_municipality_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! src/app/services/municipality.service */ "ZLEw");
/* harmony import */ var src_app_services_menu_service__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! src/app/services/menu.service */ "Gi7S");
/* harmony import */ var _auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @auth0/auth0-angular */ "2beD");
/* harmony import */ var _admin_header_admin_header_component__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ../admin-header/admin-header.component */ "auzb");
/* harmony import */ var _add_menu_add_menu_component__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! ./add-menu/add-menu.component */ "yQa+");
/* harmony import */ var _remove_menu_remove_menu_component__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! ./remove-menu/remove-menu.component */ "WPxs");








class AdminSchoolsComponent {
    constructor(municipalityService, menuService, auth) {
        this.municipalityService = municipalityService;
        this.menuService = menuService;
        this.auth = auth;
        this.subscriptions = [];
    }
    ngOnInit() {
        this.subscriptions.push(this.municipalityService.getMunicipalities().subscribe((municipalities) => {
            this.subscriptions.push(this.auth.user$.subscribe((user) => {
                let currentUser = new src_app_models_user__WEBPACK_IMPORTED_MODULE_0__["User"]();
                currentUser.setUserFromAuthPic(user.picture);
                if (!currentUser.permissions.some((perm) => perm === 'admin')) {
                    //Filter out municipalities that user have access to change
                    municipalities = municipalities.filter((mun) => {
                        return mun.schools.some((school) => {
                            return currentUser.schoolIds.some((schoolId) => schoolId === school._id);
                        });
                    });
                    //filter out schools for each municipality that user have access to change
                    municipalities.forEach((municipality) => {
                        municipality.schools = municipality.schools.filter((school) => {
                            return currentUser.schoolIds.some((schoolId) => schoolId === school._id);
                        });
                    });
                }
                this.municipalities = municipalities;
            }));
            this.municipalities = municipalities;
        }));
        this.subscriptions.push(this.menuService.getMenus().subscribe((menus) => {
            this.menus = menus;
        }));
    }
    ngOnDestroy() {
        this.subscriptions.forEach((sub) => {
            sub.unsubscribe();
        });
    }
    filterMunicipalities() {
    }
}
AdminSchoolsComponent.ɵfac = function AdminSchoolsComponent_Factory(t) { return new (t || AdminSchoolsComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](src_app_services_municipality_service__WEBPACK_IMPORTED_MODULE_2__["MunicipalityService"]), _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](src_app_services_menu_service__WEBPACK_IMPORTED_MODULE_3__["MenuService"]), _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](_auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_4__["AuthService"])); };
AdminSchoolsComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineComponent"]({ type: AdminSchoolsComponent, selectors: [["app-admin-schools"]], decls: 5, vars: 3, consts: [[1, "centered-content"], [1, "admin-content"], [3, "municipalities", "menus"], [3, "municipalities"]], template: function AdminSchoolsComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](0, "app-admin-header");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](1, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](2, "div", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](3, "app-add-menu", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](4, "app-remove-menu", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](3);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("municipalities", ctx.municipalities)("menus", ctx.menus);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](1);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("municipalities", ctx.municipalities);
    } }, directives: [_admin_header_admin_header_component__WEBPACK_IMPORTED_MODULE_5__["AdminHeaderComponent"], _add_menu_add_menu_component__WEBPACK_IMPORTED_MODULE_6__["AddMenuComponent"], _remove_menu_remove_menu_component__WEBPACK_IMPORTED_MODULE_7__["RemoveMenuComponent"]], styles: [".add-content[_ngcontent-%COMP%] {\n  justify-content: center;\n  display: flex;\n  flex-direction: columns;\n}\n\n.manage-menu-button[_ngcontent-%COMP%] {\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  padding: 20px;\n  margin: 30px;\n}\n\n.manage-menu-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.menuName[_ngcontent-%COMP%] {\n  max-width: 200px;\n  color: white;\n  padding: 20px;\n  margin: 30px;\n}\n\n.navbar-item[_ngcontent-%COMP%] {\n  margin-top: 3px;\n  cursor: pointer;\n  max-width: 250px;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%] {\n  color: white;\n  background: transparent;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%]:hover {\n  color: #1775b8;\n}\n\n.navbar-dropdown[_ngcontent-%COMP%] {\n  height: 250px;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uL2FkbWluLXNjaG9vbHMuY29tcG9uZW50LnNjc3MiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUE7RUFDRSx1QkFBQTtFQUNBLGFBQUE7RUFDQSx1QkFBQTtBQUNGOztBQUVBO0VBQ0UsV0FBQTtFQUNBLHVCQUFBO0VBQ0EsWUFBQTtFQUlBLGVBQUE7RUFDQSxhQUFBO0VBQ0EsWUFBQTtBQUZGOztBQUhFO0VBQ0UsMEJBQUE7QUFLSjs7QUFFQTtFQUNFLGdCQUFBO0VBQ0EsWUFBQTtFQUNBLGFBQUE7RUFDQSxZQUFBO0FBQ0Y7O0FBRUE7RUFDRSxlQUFBO0VBQ0EsZUFBQTtFQVFBLGdCQUFBO0FBTkY7O0FBREU7RUFDSSxZQUFBO0VBQ0EsdUJBQUE7QUFHTjs7QUFGTTtFQUNJLGNBQUE7QUFJVjs7QUFFQTtFQUNFLGFBQUE7QUFDRiIsImZpbGUiOiJhZG1pbi1zY2hvb2xzLmNvbXBvbmVudC5zY3NzIiwic291cmNlc0NvbnRlbnQiOlsiLmFkZC1jb250ZW50IHtcbiAganVzdGlmeS1jb250ZW50OiBjZW50ZXI7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiBjb2x1bW5zO1xufVxuXG4ubWFuYWdlLW1lbnUtYnV0dG9uIHtcbiAgYm9yZGVyOiAwcHg7XG4gIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICBjb2xvcjogd2hpdGU7XG4gICY6YWN0aXZlIHtcbiAgICB0cmFuc2Zvcm06IHRyYW5zbGF0ZVkoNHB4KTtcbiAgfVxuICBjdXJzb3I6IHBvaW50ZXI7XG4gIHBhZGRpbmc6IDIwcHg7XG4gIG1hcmdpbjogMzBweDtcbn1cblxuLm1lbnVOYW1lIHtcbiAgbWF4LXdpZHRoOiAyMDBweDtcbiAgY29sb3I6IHdoaXRlO1xuICBwYWRkaW5nOiAyMHB4O1xuICBtYXJnaW46IDMwcHg7XG59XG5cbi5uYXZiYXItaXRlbSB7XG4gIG1hcmdpbi10b3A6IDNweDtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICAubmF2YmFyLWxpbmsge1xuICAgICAgY29sb3I6IGhzbCgwLCAwJSwgMTAwJSk7XG4gICAgICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgICAgICY6aG92ZXIge1xuICAgICAgICAgIGNvbG9yOiAjMTc3NWI4O1xuICAgICAgfVxuICB9XG4gIG1heC13aWR0aDogMjUwcHg7XG59XG5cbi5uYXZiYXItZHJvcGRvd24ge1xuICBoZWlnaHQ6IDI1MHB4O1xufVxuIl19 */"] });


/***/ }),

/***/ "yQa+":
/*!**************************************************************************!*\
  !*** ./src/app/pages/admin/admin-schools/add-menu/add-menu.component.ts ***!
  \**************************************************************************/
/*! exports provided: AddMenuComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AddMenuComponent", function() { return AddMenuComponent; });
/* harmony import */ var _models_school__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./../../../../models/school */ "ZVnM");
/* harmony import */ var _models_menu__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./../../../../models/menu */ "N7ft");
/* harmony import */ var _models_municipality__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./../../../../models/municipality */ "Rvib");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _services_municipality_service__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./../../../../services/municipality.service */ "ZLEw");
/* harmony import */ var src_assets_alert__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! src/assets/alert */ "X440");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! @angular/forms */ "3Pt+");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! @angular/common */ "ofXK");








function AddMenuComponent_a_6_Template(rf, ctx) { if (rf & 1) {
    const _r5 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](0, "a", 8);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵlistener"]("click", function AddMenuComponent_a_6_Template_a_click_0_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵrestoreView"](_r5); const municipality_r3 = ctx.$implicit; const ctx_r4 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵnextContext"](); return ctx_r4.chooseMunicipalityToAdd(municipality_r3); });
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](1, "p");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtext"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
} if (rf & 2) {
    const municipality_r3 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtextInterpolate"](municipality_r3.municipalityName);
} }
function AddMenuComponent_a_11_Template(rf, ctx) { if (rf & 1) {
    const _r8 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](0, "a", 8);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵlistener"]("click", function AddMenuComponent_a_11_Template_a_click_0_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵrestoreView"](_r8); const school_r6 = ctx.$implicit; const ctx_r7 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵnextContext"](); return ctx_r7.chooseSchoolToAdd(school_r6); });
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](1, "p");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtext"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
} if (rf & 2) {
    const school_r6 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtextInterpolate"](school_r6.schoolName);
} }
function AddMenuComponent_a_16_Template(rf, ctx) { if (rf & 1) {
    const _r11 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](0, "a", 9);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵlistener"]("click", function AddMenuComponent_a_16_Template_a_click_0_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵrestoreView"](_r11); const menu_r9 = ctx.$implicit; const ctx_r10 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵnextContext"](); return ctx_r10.chooseMenuToAdd(menu_r9); });
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](1, "p");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtext"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
} if (rf & 2) {
    const menu_r9 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtextInterpolate"](menu_r9.menuName);
} }
class AddMenuComponent {
    constructor(municipalityService, alert) {
        this.municipalityService = municipalityService;
        this.alert = alert;
        this.municipalityToAdd = new _models_municipality__WEBPACK_IMPORTED_MODULE_2__["Municipality"]();
        this.schoolToAdd = new _models_school__WEBPACK_IMPORTED_MODULE_0__["School"]();
        this.menuToAdd = new _models_menu__WEBPACK_IMPORTED_MODULE_1__["Menu"]();
        this.chosenMunicipalityTitleToAdd = "Välj kommun: ";
        this.chosenSchoolTitleToAdd = "Välj skola: ";
        this.chosenMenuTitle = "Välj matsedel: ";
        this.subscriptions = [];
    }
    ngOnInit() {
    }
    ngOnDestroy() {
        this.subscriptions.forEach((sub) => {
            sub.unsubscribe();
        });
    }
    chooseMunicipalityToAdd(municipality) {
        this.municipalityToAdd = municipality;
        this.chosenMunicipalityTitleToAdd = municipality.municipalityName;
    }
    chooseSchoolToAdd(school) {
        this.schoolToAdd = school;
        this.chosenSchoolTitleToAdd = school.schoolName;
    }
    chooseMenuToAdd(menu) {
        this.menuToAdd = menu;
        this.chosenMenuTitle = menu.menuName;
    }
    addMenuToSchool(menu, school) {
        if (this.chosenSchoolTitleToAdd === "Välj skola: " || this.chosenMenuTitle === "Välj matsedel: ") {
            this.alert.showAlert('', 'Du måste välja både en skola och en matsedel att lägga till!', 'error');
        }
        else {
            school._menuId = menu._id;
            let sub = this.municipalityService.updateSchool(this.municipalityToAdd._id, school).subscribe(() => {
            }, (err) => this.alert.showAlert('Error', 'Något gick fel. Menyn kunde inte sparas till användaren', 'error'), () => this.alert.showAlertAndUpdatePage('Tillagd!', 'Matsedeln har blivit sparad i vald skola.', 'success'));
            this.subscriptions.push(sub);
        }
    }
}
AddMenuComponent.ɵfac = function AddMenuComponent_Factory(t) { return new (t || AddMenuComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdirectiveInject"](_services_municipality_service__WEBPACK_IMPORTED_MODULE_4__["MunicipalityService"]), _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdirectiveInject"](src_assets_alert__WEBPACK_IMPORTED_MODULE_5__["Alert"])); };
AddMenuComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdefineComponent"]({ type: AddMenuComponent, selectors: [["app-add-menu"]], inputs: { municipalities: "municipalities", menus: "menus" }, decls: 19, vars: 6, consts: [[1, "choices-content"], [1, "add-content"], [1, "navbar-item", "has-dropdown", "is-hoverable"], [1, "navbar-link"], [1, "navbar-dropdown"], ["class", "navbar-item", 3, "click", 4, "ngFor", "ngForOf"], ["class", "navbar-item menuDropDown", 3, "click", 4, "ngFor", "ngForOf"], [1, "manage-menu-button", 3, "click"], [1, "navbar-item", 3, "click"], [1, "navbar-item", "menuDropDown", 3, "click"]], template: function AddMenuComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](0, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](1, "form", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](2, "div", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](3, "a", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtext"](4);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](5, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtemplate"](6, AddMenuComponent_a_6_Template, 3, 1, "a", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](7, "div", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](8, "a", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtext"](9);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](10, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtemplate"](11, AddMenuComponent_a_11_Template, 3, 1, "a", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](12, "div", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](13, "a", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtext"](14);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](15, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtemplate"](16, AddMenuComponent_a_16_Template, 3, 1, "a", 6);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](17, "button", 7);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵlistener"]("click", function AddMenuComponent_Template_button_click_17_listener() { return ctx.addMenuToSchool(ctx.menuToAdd, ctx.schoolToAdd); });
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtext"](18, " L\u00E4gg till/ uppdatera matsedeln till skolan ");
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](4);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtextInterpolate1"](" ", ctx.chosenMunicipalityTitleToAdd, " ");
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("ngForOf", ctx.municipalities);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](3);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtextInterpolate1"](" ", ctx.chosenSchoolTitleToAdd, " ");
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("ngForOf", ctx.municipalityToAdd.schools);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](3);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtextInterpolate1"](" ", ctx.chosenMenuTitle, " ");
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("ngForOf", ctx.menus);
    } }, directives: [_angular_forms__WEBPACK_IMPORTED_MODULE_6__["ɵangular_packages_forms_forms_ba"], _angular_forms__WEBPACK_IMPORTED_MODULE_6__["NgControlStatusGroup"], _angular_forms__WEBPACK_IMPORTED_MODULE_6__["NgForm"], _angular_common__WEBPACK_IMPORTED_MODULE_7__["NgForOf"]], styles: [".add-content[_ngcontent-%COMP%] {\n  justify-content: center;\n  display: flex;\n  flex-direction: columns;\n}\n\n.manage-menu-button[_ngcontent-%COMP%] {\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  padding: 20px;\n  margin: 30px;\n}\n\n.manage-menu-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.menuName[_ngcontent-%COMP%] {\n  max-width: 200px;\n  color: white;\n  padding: 20px;\n  margin: 30px;\n}\n\n.navbar-item[_ngcontent-%COMP%] {\n  margin-top: 3px;\n  cursor: pointer;\n  max-width: 250px;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%] {\n  color: white;\n  background: transparent;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%]:hover {\n  color: #1775b8;\n}\n\n.navbar-dropdown[_ngcontent-%COMP%] {\n  height: 250px;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uLy4uL2FkbWluLXNjaG9vbHMuY29tcG9uZW50LnNjc3MiLCIuLi8uLi8uLi8uLi8uLi8uLi9hZGQtbWVudS5jb21wb25lbnQuc2NzcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTtFQUNFLHVCQUFBO0VBQ0EsYUFBQTtFQUNBLHVCQUFBO0FDQ0Y7O0FERUE7RUFDRSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxZQUFBO0VBSUEsZUFBQTtFQUNBLGFBQUE7RUFDQSxZQUFBO0FDRkY7O0FESEU7RUFDRSwwQkFBQTtBQ0tKOztBREVBO0VBQ0UsZ0JBQUE7RUFDQSxZQUFBO0VBQ0EsYUFBQTtFQUNBLFlBQUE7QUNDRjs7QURFQTtFQUNFLGVBQUE7RUFDQSxlQUFBO0VBUUEsZ0JBQUE7QUNORjs7QURERTtFQUNJLFlBQUE7RUFDQSx1QkFBQTtBQ0dOOztBREZNO0VBQ0ksY0FBQTtBQ0lWOztBREVBO0VBQ0UsYUFBQTtBQ0NGIiwiZmlsZSI6ImFkZC1tZW51LmNvbXBvbmVudC5zY3NzIiwic291cmNlc0NvbnRlbnQiOlsiLmFkZC1jb250ZW50IHtcbiAganVzdGlmeS1jb250ZW50OiBjZW50ZXI7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiBjb2x1bW5zO1xufVxuXG4ubWFuYWdlLW1lbnUtYnV0dG9uIHtcbiAgYm9yZGVyOiAwcHg7XG4gIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICBjb2xvcjogd2hpdGU7XG4gICY6YWN0aXZlIHtcbiAgICB0cmFuc2Zvcm06IHRyYW5zbGF0ZVkoNHB4KTtcbiAgfVxuICBjdXJzb3I6IHBvaW50ZXI7XG4gIHBhZGRpbmc6IDIwcHg7XG4gIG1hcmdpbjogMzBweDtcbn1cblxuLm1lbnVOYW1lIHtcbiAgbWF4LXdpZHRoOiAyMDBweDtcbiAgY29sb3I6IHdoaXRlO1xuICBwYWRkaW5nOiAyMHB4O1xuICBtYXJnaW46IDMwcHg7XG59XG5cbi5uYXZiYXItaXRlbSB7XG4gIG1hcmdpbi10b3A6IDNweDtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICAubmF2YmFyLWxpbmsge1xuICAgICAgY29sb3I6IGhzbCgwLCAwJSwgMTAwJSk7XG4gICAgICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgICAgICY6aG92ZXIge1xuICAgICAgICAgIGNvbG9yOiAjMTc3NWI4O1xuICAgICAgfVxuICB9XG4gIG1heC13aWR0aDogMjUwcHg7XG59XG5cbi5uYXZiYXItZHJvcGRvd24ge1xuICBoZWlnaHQ6IDI1MHB4O1xufVxuIiwiLmFkZC1jb250ZW50IHtcbiAganVzdGlmeS1jb250ZW50OiBjZW50ZXI7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiBjb2x1bW5zO1xufVxuXG4ubWFuYWdlLW1lbnUtYnV0dG9uIHtcbiAgYm9yZGVyOiAwcHg7XG4gIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICBjb2xvcjogd2hpdGU7XG4gIGN1cnNvcjogcG9pbnRlcjtcbiAgcGFkZGluZzogMjBweDtcbiAgbWFyZ2luOiAzMHB4O1xufVxuLm1hbmFnZS1tZW51LWJ1dHRvbjphY3RpdmUge1xuICB0cmFuc2Zvcm06IHRyYW5zbGF0ZVkoNHB4KTtcbn1cblxuLm1lbnVOYW1lIHtcbiAgbWF4LXdpZHRoOiAyMDBweDtcbiAgY29sb3I6IHdoaXRlO1xuICBwYWRkaW5nOiAyMHB4O1xuICBtYXJnaW46IDMwcHg7XG59XG5cbi5uYXZiYXItaXRlbSB7XG4gIG1hcmdpbi10b3A6IDNweDtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICBtYXgtd2lkdGg6IDI1MHB4O1xufVxuLm5hdmJhci1pdGVtIC5uYXZiYXItbGluayB7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG59XG4ubmF2YmFyLWl0ZW0gLm5hdmJhci1saW5rOmhvdmVyIHtcbiAgY29sb3I6ICMxNzc1Yjg7XG59XG5cbi5uYXZiYXItZHJvcGRvd24ge1xuICBoZWlnaHQ6IDI1MHB4O1xufSJdfQ== */"] });


/***/ }),

/***/ "ySEZ":
/*!*****************************************************************************!*\
  !*** ./src/app/pages/admin/admin-user/user-header/user-header.component.ts ***!
  \*****************************************************************************/
/*! exports provided: UserHeaderComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "UserHeaderComponent", function() { return UserHeaderComponent; });
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/router */ "tyNb");


class UserHeaderComponent {
    constructor() { }
    ngOnInit() {
    }
}
UserHeaderComponent.ɵfac = function UserHeaderComponent_Factory(t) { return new (t || UserHeaderComponent)(); };
UserHeaderComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineComponent"]({ type: UserHeaderComponent, selectors: [["app-user-header"]], decls: 8, vars: 0, consts: [[1, "centered-content"], [1, "header-content"], ["routerLink", "/createUser", "routerLinkActive", "selected", 1, "create-button"], ["routerLink", "/updateUser", "routerLinkActive", "selected", 1, "update-button"], ["routerLink", "/deleteUser", "routerLinkActive", "selected", 1, "delete-button"]], template: function UserHeaderComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](0, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](1, "div", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](2, "button", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](3, " L\u00E4gg till anv\u00E4ndare ");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](4, "button", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](5, " Uppdatera anv\u00E4ndare ");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](6, "button", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](7, " Ta bort anv\u00E4ndare ");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
    } }, directives: [_angular_router__WEBPACK_IMPORTED_MODULE_1__["RouterLink"], _angular_router__WEBPACK_IMPORTED_MODULE_1__["RouterLinkActive"]], styles: [".centered-content[_ngcontent-%COMP%] {\n  height: 100%;\n  max-height: 50px;\n  min-height: 50px;\n  padding: 15px;\n}\n\n.header-content[_ngcontent-%COMP%] {\n  display: flex;\n  padding: 15px;\n}\n\n.create-button[_ngcontent-%COMP%] {\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  padding: 20px;\n  margin-right: 60px;\n}\n\n.create-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.update-button[_ngcontent-%COMP%] {\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  padding: 20px;\n}\n\n.update-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.delete-button[_ngcontent-%COMP%] {\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  padding: 20px;\n  margin-left: 60px;\n}\n\n.delete-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.selected[_ngcontent-%COMP%] {\n  color: #1775b8;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uL3VzZXItaGVhZGVyLmNvbXBvbmVudC5zY3NzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBO0VBQ0UsWUFBQTtFQUNBLGdCQUFBO0VBQ0EsZ0JBQUE7RUFDQSxhQUFBO0FBQ0Y7O0FBRUE7RUFDRSxhQUFBO0VBQ0EsYUFBQTtBQUNGOztBQUVBO0VBQ0UsV0FBQTtFQUNBLHVCQUFBO0VBQ0EsWUFBQTtFQUlBLGVBQUE7RUFDQSxhQUFBO0VBQ0Esa0JBQUE7QUFGRjs7QUFIRTtFQUNFLDBCQUFBO0FBS0o7O0FBRUE7RUFDRSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxZQUFBO0VBSUEsZUFBQTtFQUNBLGFBQUE7QUFGRjs7QUFGRTtFQUNFLDBCQUFBO0FBSUo7O0FBRUE7RUFDRSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxZQUFBO0VBSUEsZUFBQTtFQUNBLGFBQUE7RUFDQSxpQkFBQTtBQUZGOztBQUhFO0VBQ0UsMEJBQUE7QUFLSjs7QUFFQTtFQUNFLGNBQUE7QUFDRiIsImZpbGUiOiJ1c2VyLWhlYWRlci5jb21wb25lbnQuc2NzcyIsInNvdXJjZXNDb250ZW50IjpbIi5jZW50ZXJlZC1jb250ZW50IHtcbiAgaGVpZ2h0OiAxMDAlO1xuICBtYXgtaGVpZ2h0OiA1MHB4O1xuICBtaW4taGVpZ2h0OiA1MHB4O1xuICBwYWRkaW5nOiAxNXB4O1xufVxuXG4uaGVhZGVyLWNvbnRlbnQge1xuICBkaXNwbGF5OiBmbGV4O1xuICBwYWRkaW5nOiAxNXB4O1xufVxuXG4uY3JlYXRlLWJ1dHRvbiB7XG4gIGJvcmRlcjogMHB4O1xuICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgY29sb3I6IHdoaXRlO1xuICAmOmFjdGl2ZSB7XG4gICAgdHJhbnNmb3JtOiB0cmFuc2xhdGVZKDRweCk7XG4gIH1cbiAgY3Vyc29yOiBwb2ludGVyO1xuICBwYWRkaW5nOiAyMHB4O1xuICBtYXJnaW4tcmlnaHQ6IDYwcHg7XG59XG5cbi51cGRhdGUtYnV0dG9uIHtcbiAgYm9yZGVyOiAwcHg7XG4gIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICBjb2xvcjogd2hpdGU7XG4gICY6YWN0aXZlIHtcbiAgICB0cmFuc2Zvcm06IHRyYW5zbGF0ZVkoNHB4KTtcbiAgfVxuICBjdXJzb3I6IHBvaW50ZXI7XG4gIHBhZGRpbmc6IDIwcHg7XG59XG5cbi5kZWxldGUtYnV0dG9uIHtcbiAgYm9yZGVyOiAwcHg7XG4gIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICBjb2xvcjogd2hpdGU7XG4gICY6YWN0aXZlIHtcbiAgICB0cmFuc2Zvcm06IHRyYW5zbGF0ZVkoNHB4KTtcbiAgfVxuICBjdXJzb3I6IHBvaW50ZXI7XG4gIHBhZGRpbmc6IDIwcHg7XG4gIG1hcmdpbi1sZWZ0OiA2MHB4O1xufVxuXG4uc2VsZWN0ZWQge1xuICBjb2xvcjogIzE3NzViODtcbn1cbiJdfQ== */"] });


/***/ }),

/***/ "yy7p":
/*!******************************************************************************!*\
  !*** ./src/app/pages/admin/admin-menus/create-menu/create-menu.component.ts ***!
  \******************************************************************************/
/*! exports provided: CreateMenuComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "CreateMenuComponent", function() { return CreateMenuComponent; });
/* harmony import */ var _models_menu__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./../../../../models/menu */ "N7ft");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/common */ "ofXK");
/* harmony import */ var src_app_models_user__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! src/app/models/user */ "2hxB");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _services_menu_service__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! ./../../../../services/menu.service */ "Gi7S");
/* harmony import */ var src_assets_alert__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! src/assets/alert */ "X440");
/* harmony import */ var _auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! @auth0/auth0-angular */ "2beD");
/* harmony import */ var src_app_services_user_service__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! src/app/services/user.service */ "qfBg");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! @angular/forms */ "3Pt+");










class CreateMenuComponent {
    constructor(menuService, alert, auth, userService) {
        this.menuService = menuService;
        this.alert = alert;
        this.auth = auth;
        this.userService = userService;
        this.currentDate = new Date();
        this.subscriptions = [];
    }
    ngOnInit() {
    }
    ngOnDestroy() {
        this.subscriptions.forEach((sub) => {
            sub.unsubscribe();
        });
    }
    createMenu(menuName, startDate, endDate) {
        let datePipe = new _angular_common__WEBPACK_IMPORTED_MODULE_1__["DatePipe"]('en-US');
        if (menuName.length < 1) {
            this.alert.showAlert('', 'Input för menynamnet är för kort. Testa igen!', 'error');
        }
        else if (menuName.length > 40) {
            this.alert.showAlert('', 'Input för menynamnet är för långt. Testa igen!', 'error');
        }
        else {
            let menu = new _models_menu__WEBPACK_IMPORTED_MODULE_0__["Menu"]();
            menu.menuName = menuName;
            menu.startDate = startDate;
            menu.endDate = endDate;
            this.subscriptions.push(this.menuService.postMenu(menu).subscribe((createdMenu) => {
                this.subscriptions.push(this.auth.user$.subscribe((user) => {
                    let currentUser = new src_app_models_user__WEBPACK_IMPORTED_MODULE_2__["User"]();
                    currentUser.setUserFromAuthPic(user.picture);
                    currentUser.menuIds.push(createdMenu._id);
                    this.subscriptions.push(this.userService.updateUser(currentUser).subscribe(() => {
                    }, (err) => this.alert.showAlert('Error', 'Något gick fel. Menyn kunde inte sparas till användaren', 'error')));
                }));
            }, (err) => this.alert.showAlert('Error', 'Något gick fel. Menyn kunde inte sparas', 'error'), () => this.alert.showAlertAndUpdatePage('Sparad!', 'Matsedeln har blivit sparad.', 'success')));
        }
    }
}
CreateMenuComponent.ɵfac = function CreateMenuComponent_Factory(t) { return new (t || CreateMenuComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdirectiveInject"](_services_menu_service__WEBPACK_IMPORTED_MODULE_4__["MenuService"]), _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdirectiveInject"](src_assets_alert__WEBPACK_IMPORTED_MODULE_5__["Alert"]), _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdirectiveInject"](_auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_6__["AuthService"]), _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdirectiveInject"](src_app_services_user_service__WEBPACK_IMPORTED_MODULE_7__["UserService"])); };
CreateMenuComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdefineComponent"]({ type: CreateMenuComponent, selectors: [["app-create-menu"]], decls: 19, vars: 8, consts: [[1, "choices-content"], [1, "add-content"], [1, "test"], ["type", "text", "placeholder", "Ange matsedelns namn: ", 1, "input-field"], ["menuName", ""], [1, "dates"], ["for", "start", 1, "date-label"], ["type", "date", "id", "start", "name", "start-date", "min", "2021-01-01", "max", "2030-12-31", 1, "date-input", 3, "value"], ["startDate", ""], ["type", "date", "id", "end", "name", "end-date", "min", "2021-01-01", "max", "2030-12-31", 1, "date-input", 3, "value"], ["endDate", ""], [1, "button-test"], [1, "manage-menu-button", 3, "click"]], template: function CreateMenuComponent_Template(rf, ctx) { if (rf & 1) {
        const _r3 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵgetCurrentView"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](0, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](1, "form", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](2, "div", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](3, "input", 3, 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](5, "div", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](6, "label", 6);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtext"](7, "Startdatum: ");
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](8, "input", 7, 8);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵpipe"](10, "date");
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](11, "label", 6);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtext"](12, "Slutdatum: ");
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](13, "input", 9, 10);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵpipe"](15, "date");
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](16, "div", 11);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](17, "button", 12);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵlistener"]("click", function CreateMenuComponent_Template_button_click_17_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵrestoreView"](_r3); const _r0 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵreference"](4); const _r1 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵreference"](9); const _r2 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵreference"](14); return ctx.createMenu(_r0.value, _r1.value, _r2.value); });
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtext"](18, " Skapa matsedel ");
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](8);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("value", _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵpipeBind2"](10, 2, ctx.currentDate, "yyyy-MM-dd"));
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](5);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("value", _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵpipeBind2"](15, 5, ctx.currentDate, "yyyy-MM-dd"));
    } }, directives: [_angular_forms__WEBPACK_IMPORTED_MODULE_8__["ɵangular_packages_forms_forms_ba"], _angular_forms__WEBPACK_IMPORTED_MODULE_8__["NgControlStatusGroup"], _angular_forms__WEBPACK_IMPORTED_MODULE_8__["NgForm"]], pipes: [_angular_common__WEBPACK_IMPORTED_MODULE_1__["DatePipe"]], styles: [".choices-content[_ngcontent-%COMP%] {\n  vertical-align: middle;\n  align-items: center;\n  flex-direction: row;\n}\n\n.input-field[_ngcontent-%COMP%] {\n  background: #08496d;\n  border: 0px;\n  border-radius: 8px;\n  color: white;\n  width: 250px;\n  height: 18px;\n  margin: 10px;\n  padding: 8px;\n}\n\n.manage-menu-button[_ngcontent-%COMP%] {\n  width: 190px;\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  margin-left: 100px;\n}\n\n.manage-menu-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.date-input[_ngcontent-%COMP%] {\n  background: #08496d;\n  border: 0px;\n  border-radius: 8px;\n  color: white;\n  width: 150px;\n  height: 20px;\n  padding: 10px;\n  margin-right: 15px;\n}\n\n.date-label[_ngcontent-%COMP%] {\n  color: white;\n  height: 20px;\n  margin-right: 5px;\n}\n\n.navbar-item[_ngcontent-%COMP%] {\n  margin-top: 3px;\n  cursor: pointer;\n  max-width: 250px;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%] {\n  color: white;\n  background: transparent;\n}\n\n.navbar-item[_ngcontent-%COMP%]   .navbar-link[_ngcontent-%COMP%]:hover {\n  color: #1775b8;\n}\n\n.navbar-dropdown[_ngcontent-%COMP%] {\n  height: 210px;\n}\n\n.add-content[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: row;\n  width: 100%;\n  height: 100%;\n}\n\n.dates[_ngcontent-%COMP%] {\n  display: flex;\n  padding: 5px;\n}\n\n.test[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: column;\n}\n\n.button-test[_ngcontent-%COMP%] {\n  display: flex;\n  justify-content: right;\n}\n\n.manage-menu-button-delete[_ngcontent-%COMP%] {\n  width: 190px;\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  margin-left: 390px;\n}\n\n.manage-menu-button-delete[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uLy4uL2FkbWluLW1lbnVzLmNvbXBvbmVudC5zY3NzIiwiLi4vLi4vLi4vLi4vLi4vLi4vY3JlYXRlLW1lbnUuY29tcG9uZW50LnNjc3MiXSwibmFtZXMiOltdLCJtYXBwaW5ncyI6IkFBQUE7RUFDRSxzQkFBQTtFQUNBLG1CQUFBO0VBQ0EsbUJBQUE7QUNDRjs7QURFQTtFQUNFLG1CQUFBO0VBQ0EsV0FBQTtFQUNBLGtCQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7RUFDQSxZQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7QUNDRjs7QURFQTtFQUNFLFlBQUE7RUFDQSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxZQUFBO0VBSUEsZUFBQTtFQUNBLGtCQUFBO0FDRkY7O0FERkU7RUFDRSwwQkFBQTtBQ0lKOztBREVBO0VBQ0UsbUJBQUE7RUFDQSxXQUFBO0VBQ0Esa0JBQUE7RUFDQSxZQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7RUFDQSxhQUFBO0VBQ0Esa0JBQUE7QUNDRjs7QURFQTtFQUNFLFlBQUE7RUFDQSxZQUFBO0VBQ0EsaUJBQUE7QUNDRjs7QURFQTtFQUNFLGVBQUE7RUFDQSxlQUFBO0VBUUEsZ0JBQUE7QUNORjs7QURERTtFQUNJLFlBQUE7RUFDQSx1QkFBQTtBQ0dOOztBREZNO0VBQ0ksY0FBQTtBQ0lWOztBREVBO0VBQ0UsYUFBQTtBQ0NGOztBREVBO0VBQ0UsYUFBQTtFQUNBLG1CQUFBO0VBQ0EsV0FBQTtFQUNBLFlBQUE7QUNDRjs7QURFQTtFQUNFLGFBQUE7RUFDQSxZQUFBO0FDQ0Y7O0FERUE7RUFDRSxhQUFBO0VBQ0Esc0JBQUE7QUNDRjs7QURFQTtFQUNFLGFBQUE7RUFDQSxzQkFBQTtBQ0NGOztBREVBO0VBQ0csWUFBQTtFQUNBLFdBQUE7RUFDQSx1QkFBQTtFQUNBLFlBQUE7RUFJQSxlQUFBO0VBQ0Esa0JBQUE7QUNGSDs7QURGRztFQUNFLDBCQUFBO0FDSUwiLCJmaWxlIjoiY3JlYXRlLW1lbnUuY29tcG9uZW50LnNjc3MiLCJzb3VyY2VzQ29udGVudCI6WyIuY2hvaWNlcy1jb250ZW50IHtcbiAgdmVydGljYWwtYWxpZ246IG1pZGRsZTtcbiAgYWxpZ24taXRlbXM6IGNlbnRlcjtcbiAgZmxleC1kaXJlY3Rpb246IHJvdztcbn1cblxuLmlucHV0LWZpZWxkIHtcbiAgYmFja2dyb3VuZDogIzA4NDk2ZDtcbiAgYm9yZGVyOiAwcHg7XG4gIGJvcmRlci1yYWRpdXM6IDhweDtcbiAgY29sb3I6IHdoaXRlO1xuICB3aWR0aDogMjUwcHg7XG4gIGhlaWdodDogMThweDtcbiAgbWFyZ2luOiAxMHB4O1xuICBwYWRkaW5nOiA4cHg7XG59XG5cbi5tYW5hZ2UtbWVudS1idXR0b24ge1xuICB3aWR0aDogMTkwcHg7XG4gIGJvcmRlcjogMHB4O1xuICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgY29sb3I6IHdoaXRlO1xuICAmOmFjdGl2ZSB7XG4gICAgdHJhbnNmb3JtOiB0cmFuc2xhdGVZKDRweCk7XG4gIH1cbiAgY3Vyc29yOiBwb2ludGVyO1xuICBtYXJnaW4tbGVmdDogMTAwcHg7XG59XG5cbi5kYXRlLWlucHV0IHtcbiAgYmFja2dyb3VuZDogIzA4NDk2ZDtcbiAgYm9yZGVyOiAwcHg7XG4gIGJvcmRlci1yYWRpdXM6IDhweDtcbiAgY29sb3I6IHdoaXRlO1xuICB3aWR0aDogMTUwcHg7XG4gIGhlaWdodDogMjBweDtcbiAgcGFkZGluZzogMTBweDtcbiAgbWFyZ2luLXJpZ2h0OiAxNXB4O1xufVxuXG4uZGF0ZS1sYWJlbHtcbiAgY29sb3I6IHdoaXRlO1xuICBoZWlnaHQ6IDIwcHg7XG4gIG1hcmdpbi1yaWdodDogNXB4O1xufVxuXG4ubmF2YmFyLWl0ZW0ge1xuICBtYXJnaW4tdG9wOiAzcHg7XG4gIGN1cnNvcjogcG9pbnRlcjtcbiAgLm5hdmJhci1saW5rIHtcbiAgICAgIGNvbG9yOiBoc2woMCwgMCUsIDEwMCUpO1xuICAgICAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gICAgICAmOmhvdmVyIHtcbiAgICAgICAgICBjb2xvcjogIzE3NzViODtcbiAgICAgIH1cbiAgfVxuICBtYXgtd2lkdGg6IDI1MHB4O1xufVxuXG4ubmF2YmFyLWRyb3Bkb3duIHtcbiAgaGVpZ2h0OiAyMTBweDtcbn1cblxuLmFkZC1jb250ZW50IHtcbiAgZGlzcGxheTogZmxleDtcbiAgZmxleC1kaXJlY3Rpb246IHJvdztcbiAgd2lkdGg6IDEwMCU7XG4gIGhlaWdodDogMTAwJTtcbn1cblxuLmRhdGVzIHtcbiAgZGlzcGxheTogZmxleDtcbiAgcGFkZGluZzogNXB4O1xufVxuXG4udGVzdCB7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiBjb2x1bW47XG59XG5cbi5idXR0b24tdGVzdCB7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGp1c3RpZnktY29udGVudDogcmlnaHQ7XG59XG5cbi5tYW5hZ2UtbWVudS1idXR0b24tZGVsZXRlIHtcbiAgIHdpZHRoOiAxOTBweDtcbiAgIGJvcmRlcjogMHB4O1xuICAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gICBjb2xvcjogd2hpdGU7XG4gICAmOmFjdGl2ZSB7XG4gICAgIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xuICAgfVxuICAgY3Vyc29yOiBwb2ludGVyO1xuICAgbWFyZ2luLWxlZnQ6IDM5MHB4O1xufVxuIiwiLmNob2ljZXMtY29udGVudCB7XG4gIHZlcnRpY2FsLWFsaWduOiBtaWRkbGU7XG4gIGFsaWduLWl0ZW1zOiBjZW50ZXI7XG4gIGZsZXgtZGlyZWN0aW9uOiByb3c7XG59XG5cbi5pbnB1dC1maWVsZCB7XG4gIGJhY2tncm91bmQ6ICMwODQ5NmQ7XG4gIGJvcmRlcjogMHB4O1xuICBib3JkZXItcmFkaXVzOiA4cHg7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgd2lkdGg6IDI1MHB4O1xuICBoZWlnaHQ6IDE4cHg7XG4gIG1hcmdpbjogMTBweDtcbiAgcGFkZGluZzogOHB4O1xufVxuXG4ubWFuYWdlLW1lbnUtYnV0dG9uIHtcbiAgd2lkdGg6IDE5MHB4O1xuICBib3JkZXI6IDBweDtcbiAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICBtYXJnaW4tbGVmdDogMTAwcHg7XG59XG4ubWFuYWdlLW1lbnUtYnV0dG9uOmFjdGl2ZSB7XG4gIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xufVxuXG4uZGF0ZS1pbnB1dCB7XG4gIGJhY2tncm91bmQ6ICMwODQ5NmQ7XG4gIGJvcmRlcjogMHB4O1xuICBib3JkZXItcmFkaXVzOiA4cHg7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgd2lkdGg6IDE1MHB4O1xuICBoZWlnaHQ6IDIwcHg7XG4gIHBhZGRpbmc6IDEwcHg7XG4gIG1hcmdpbi1yaWdodDogMTVweDtcbn1cblxuLmRhdGUtbGFiZWwge1xuICBjb2xvcjogd2hpdGU7XG4gIGhlaWdodDogMjBweDtcbiAgbWFyZ2luLXJpZ2h0OiA1cHg7XG59XG5cbi5uYXZiYXItaXRlbSB7XG4gIG1hcmdpbi10b3A6IDNweDtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICBtYXgtd2lkdGg6IDI1MHB4O1xufVxuLm5hdmJhci1pdGVtIC5uYXZiYXItbGluayB7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG59XG4ubmF2YmFyLWl0ZW0gLm5hdmJhci1saW5rOmhvdmVyIHtcbiAgY29sb3I6ICMxNzc1Yjg7XG59XG5cbi5uYXZiYXItZHJvcGRvd24ge1xuICBoZWlnaHQ6IDIxMHB4O1xufVxuXG4uYWRkLWNvbnRlbnQge1xuICBkaXNwbGF5OiBmbGV4O1xuICBmbGV4LWRpcmVjdGlvbjogcm93O1xuICB3aWR0aDogMTAwJTtcbiAgaGVpZ2h0OiAxMDAlO1xufVxuXG4uZGF0ZXMge1xuICBkaXNwbGF5OiBmbGV4O1xuICBwYWRkaW5nOiA1cHg7XG59XG5cbi50ZXN0IHtcbiAgZGlzcGxheTogZmxleDtcbiAgZmxleC1kaXJlY3Rpb246IGNvbHVtbjtcbn1cblxuLmJ1dHRvbi10ZXN0IHtcbiAgZGlzcGxheTogZmxleDtcbiAganVzdGlmeS1jb250ZW50OiByaWdodDtcbn1cblxuLm1hbmFnZS1tZW51LWJ1dHRvbi1kZWxldGUge1xuICB3aWR0aDogMTkwcHg7XG4gIGJvcmRlcjogMHB4O1xuICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgY29sb3I6IHdoaXRlO1xuICBjdXJzb3I6IHBvaW50ZXI7XG4gIG1hcmdpbi1sZWZ0OiAzOTBweDtcbn1cbi5tYW5hZ2UtbWVudS1idXR0b24tZGVsZXRlOmFjdGl2ZSB7XG4gIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xufSJdfQ== */"] });


/***/ }),

/***/ "z+UM":
/*!******************************************************************!*\
  !*** ./src/app/pages/admin/admin-meals/admin-meals.component.ts ***!
  \******************************************************************/
/*! exports provided: AdminMealsComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "AdminMealsComponent", function() { return AdminMealsComponent; });
/* harmony import */ var src_app_models_meal__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! src/app/models/meal */ "hi36");
/* harmony import */ var uuid__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! uuid */ "EcEN");
/* harmony import */ var uuid__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(uuid__WEBPACK_IMPORTED_MODULE_1__);
/* harmony import */ var src_app_models_user__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! src/app/models/user */ "2hxB");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var src_app_services_sharing_service__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! src/app/services/sharing.service */ "KxZz");
/* harmony import */ var src_app_services_menu_service__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! src/app/services/menu.service */ "Gi7S");
/* harmony import */ var src_assets_alert__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! src/assets/alert */ "X440");
/* harmony import */ var _auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_7__ = __webpack_require__(/*! @auth0/auth0-angular */ "2beD");
/* harmony import */ var _admin_header_admin_header_component__WEBPACK_IMPORTED_MODULE_8__ = __webpack_require__(/*! ../admin-header/admin-header.component */ "auzb");
/* harmony import */ var _header_header_component__WEBPACK_IMPORTED_MODULE_9__ = __webpack_require__(/*! ../../header/header.component */ "/QED");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_10__ = __webpack_require__(/*! @angular/common */ "ofXK");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_11__ = __webpack_require__(/*! @angular/forms */ "3Pt+");












function AdminMealsComponent_div_2_div_3_ng_template_9_div_0_Template(rf, ctx) { if (rf & 1) {
    const _r14 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](0, "div", 10);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](1, "form");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](2, "input", 11, 12);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](4, "input", 13, 14);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](6, "i", 15);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](7, "input", 16, 17);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](9, "i", 18);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](10, "input", 19, 20);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](12, "i", 21);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](13, "button", 22, 23);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵlistener"]("click", function AdminMealsComponent_div_2_div_3_ng_template_9_div_0_Template_button_click_13_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵrestoreView"](_r14); const meal_r6 = ctx.$implicit; const _r7 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵreference"](3); const _r8 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵreference"](5); const _r9 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵreference"](8); const _r10 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵreference"](11); const day_r2 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵnextContext"](2).$implicit; const ctx_r12 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵnextContext"](2); return ctx_r12.updateMeal(meal_r6, day_r2, _r7.value, _r8, _r9, _r10); });
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](15, "i", 24);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](16, "button", 25);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵlistener"]("click", function AdminMealsComponent_div_2_div_3_ng_template_9_div_0_Template_button_click_16_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵrestoreView"](_r14); const meal_r6 = ctx.$implicit; const day_r2 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵnextContext"](2).$implicit; const ctx_r15 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵnextContext"](2); return ctx_r15.deleteMeal(meal_r6._id, day_r2); });
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](17, "i", 26);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
} if (rf & 2) {
    const meal_r6 = ctx.$implicit;
    const ctx_r4 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵnextContext"](4);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("value", meal_r6.mealName);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("checked", ctx_r4.checkFoodSpec(meal_r6.foodSpecs, "veg"));
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](3);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("checked", ctx_r4.checkFoodSpec(meal_r6.foodSpecs, "hot"));
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](3);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("checked", ctx_r4.checkFoodSpec(meal_r6.foodSpecs, "pig"));
} }
function AdminMealsComponent_div_2_div_3_ng_template_9_div_1_Template(rf, ctx) { if (rf & 1) {
    const _r24 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](0, "div", 27);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](1, "form", null, 28);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](3, "input", 29, 30);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](5, "input", 31, 14);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](7, "i", 15);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](8, "input", 32, 17);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](10, "i", 18);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](11, "input", 33, 20);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](13, "i", 21);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](14, "button", 34);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵlistener"]("click", function AdminMealsComponent_div_2_div_3_ng_template_9_div_1_Template_button_click_14_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵrestoreView"](_r24); const _r18 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵreference"](4); const _r19 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵreference"](6); const _r20 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵreference"](9); const _r21 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵreference"](12); const _r17 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵreference"](2); const day_r2 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵnextContext"](2).$implicit; const ctx_r22 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵnextContext"](2); return ctx_r22.saveMeal(day_r2, _r18.value, _r19, _r20, _r21, _r17); });
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](15, "i", 35);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
} }
function AdminMealsComponent_div_2_div_3_ng_template_9_Template(rf, ctx) { if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtemplate"](0, AdminMealsComponent_div_2_div_3_ng_template_9_div_0_Template, 18, 4, "div", 8);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtemplate"](1, AdminMealsComponent_div_2_div_3_ng_template_9_div_1_Template, 16, 0, "div", 9);
} if (rf & 2) {
    const day_r2 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵnextContext"]().$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("ngForOf", day_r2.meals);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](1);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("ngIf", day_r2.meals.length < 4);
} }
function AdminMealsComponent_div_2_div_3_Template(rf, ctx) { if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](0, "div", 4);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](1, "div", 5);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtext"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵpipe"](3, "titlecase");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵpipe"](4, "date");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](5, "br");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtext"](6);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵpipe"](7, "date");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](8, "div", 6);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtemplate"](9, AdminMealsComponent_div_2_div_3_ng_template_9_Template, 2, 2, "ng-template", 7);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
} if (rf & 2) {
    const day_r2 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtextInterpolate1"](" ", _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵpipeBind1"](3, 3, _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵpipeBind2"](4, 5, day_r2.date, "EEEE")), "");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](4);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtextInterpolate1"]("", _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵpipeBind1"](7, 8, day_r2.date), " ");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](3);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("ngIf", day_r2.meals);
} }
function AdminMealsComponent_div_2_Template(rf, ctx) { if (rf & 1) {
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](0, "div");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](1, "div", 1);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementStart"](2, "div", 2);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtemplate"](3, AdminMealsComponent_div_2_div_3_Template, 10, 10, "div", 3);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](4, "br");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](5, "br");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](6, "br");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](7, "br");
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelementEnd"]();
} if (rf & 2) {
    const ctx_r0 = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵnextContext"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](3);
    _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("ngForOf", ctx_r0.week.days);
} }
class AdminMealsComponent {
    constructor(sharingService, menuService, alert, auth) {
        this.sharingService = sharingService;
        this.menuService = menuService;
        this.alert = alert;
        this.auth = auth;
        this.subscriptions = [];
    }
    ngOnInit() {
        this.subscriptions.push(this.auth.user$.subscribe((user) => {
            this.currentUser = new src_app_models_user__WEBPACK_IMPORTED_MODULE_2__["User"]();
            this.currentUser.setUserFromAuthPic(user.picture);
        }));
        let sub = this.sharingService.getObservableWeek().subscribe((week) => {
            this.week = week;
        });
        this.subscriptions.push(sub);
        let sub2 = this.sharingService.getObservableMenu().subscribe((menu) => {
            this.menu = menu;
        });
        this.subscriptions.push(sub2);
    }
    ngOnDestroy() {
        this.subscriptions.forEach(sub => {
            sub.unsubscribe();
        });
    }
    deleteMeal(mealId, day) {
        if (!this.checkPermissions) {
            this.alert.showAlert('', 'Du måste ha behörighet för att administrera denna matsedel!', 'error');
        }
        else {
            this.alert.showAdvancedAlert('VARNING', 'Vill du ta bort denna maträtt?', 'warning', 'Ja, ta bort', 'Avbryt').then((result) => {
                if (result.isConfirmed) {
                    day.meals.forEach((meal, index) => {
                        if (meal._id === mealId) {
                            day.meals.splice(index, 1);
                        }
                    });
                    let sub = this.menuService.deleteMeal(this.menu._id, mealId).subscribe(() => {
                    }, (err) => this.alert.showAlert('Error', 'Något gick fel. Maträtten kunde inte tas bort från databasen', 'error'));
                    this.subscriptions.push(sub);
                    this.alert.showAlert('Borttagen!', 'Vald maträtt har blivit borttagen.', 'success');
                }
            });
        }
    }
    checkFoodSpec(foodSpecs, wantedSpec) {
        return foodSpecs.some((spec) => spec === wantedSpec);
    }
    updateMeal(meal, day, mealName, veg, hot, pig) {
        if (!this.checkPermissions) {
            this.alert.showAlert('', 'Du måste ha behörighet för att administrera denna matsedel!', 'error');
        }
        else if (this.checkMealLength(mealName)) {
            meal.mealName = mealName;
            meal.foodSpecs = [];
            if (veg.checked) {
                meal.foodSpecs.push(veg.value);
            }
            if (hot.checked) {
                meal.foodSpecs.push(hot.value);
            }
            if (pig.checked) {
                meal.foodSpecs.push(pig.value);
            }
            let sub = this.menuService.updateMeal(meal, this.menu._id).subscribe(() => { }, (err) => this.alert.showAlert('Error', 'Något gick fel. Maträtten kunde inte updateras i databasen', 'error'));
            this.subscriptions.push(sub);
            this.alert.showAlert('Uppdaterad!', 'Maträtten har blivit uppdaterad.', 'success');
        }
    }
    saveMeal(day, newMealName, veg, hot, pig, form) {
        if (!this.checkPermissions) {
            this.alert.showAlert('', 'Du måste ha behörighet för att administrera denna matsedel!', 'error');
        }
        else if (this.checkMealLength(newMealName)) {
            let meal = new src_app_models_meal__WEBPACK_IMPORTED_MODULE_0__["Meal"]();
            meal.mealName = newMealName;
            meal.mealDate = new Date(day.date);
            if (veg.checked) {
                meal.foodSpecs.push(veg.value);
            }
            if (hot.checked) {
                meal.foodSpecs.push(hot.value);
            }
            if (pig.checked) {
                meal.foodSpecs.push(pig.value);
            }
            meal._id = Object(uuid__WEBPACK_IMPORTED_MODULE_1__["v4"])();
            day.meals.push(meal);
            let sub = this.menuService.postMeal(meal, this.menu._id).subscribe((mealId) => {
            }, (err) => this.alert.showAlert('Error', 'Något gick fel. Maträtten kunde inte sparas i databasen', 'error'));
            this.subscriptions.push(sub);
            form.reset();
            this.alert.showAlert('Sparad!', 'Maträtten har blivit sparad.', 'success');
        }
    }
    checkPermissions() {
        return this.currentUser.permissions.some((permission) => permission === 'admin') && !this.currentUser.menuIds.some((menuId) => menuId === this.menu._id);
    }
    checkMealLength(newMealName) {
        if (newMealName.length < 1) {
            this.alert.showAlert('', 'Input för maträtten är för kort. Testa igen!', 'error');
            return false;
        }
        else if (newMealName.length > 85) {
            this.alert.showAlert('', 'Input för maträtten är för långt. Testa igen!', 'error');
            return false;
        }
        else
            return true;
    }
}
AdminMealsComponent.ɵfac = function AdminMealsComponent_Factory(t) { return new (t || AdminMealsComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdirectiveInject"](src_app_services_sharing_service__WEBPACK_IMPORTED_MODULE_4__["SharingService"]), _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdirectiveInject"](src_app_services_menu_service__WEBPACK_IMPORTED_MODULE_5__["MenuService"]), _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdirectiveInject"](src_assets_alert__WEBPACK_IMPORTED_MODULE_6__["Alert"]), _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdirectiveInject"](_auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_7__["AuthService"])); };
AdminMealsComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵdefineComponent"]({ type: AdminMealsComponent, selectors: [["app-admin"]], decls: 3, vars: 1, consts: [[4, "ngIf"], [1, "centered-content"], [1, "menu-content"], ["class", "day-content", 4, "ngFor", "ngForOf"], [1, "day-content"], [1, "day-date"], [1, "meals-content"], [3, "ngIf"], ["class", "day-meals", 4, "ngFor", "ngForOf"], ["class", "day-admin-meals", 4, "ngIf"], [1, "day-meals"], ["type", "text", 1, "update-meal-input-field", 3, "value"], ["mealName", ""], ["title", "Vegetarisk", "type", "checkbox", "value", "veg", "name", "veg", "id", "veg", 1, "icon-checkbox", 3, "checked"], ["veg", ""], ["title", "Vegetarisk", 1, "fas", "veg", "fa-seedling"], ["title", "Stark", "type", "checkbox", "value", "hot", "name", "hot", "id", "hot", 1, "icon-checkbox", 3, "checked"], ["hot", ""], ["title", "Stark", 1, "fas", "hot", "fa-pepper-hot"], ["title", "Fl\u00E4sk", "type", "checkbox", "value", "pig", "name", "pig", "id", "pig", 1, "icon-checkbox", 3, "checked"], ["pig", ""], ["title", "Fl\u00E4sk", 1, "fas", "pig", "fa-bacon"], ["title", "Uppdatera", 1, "update-button", 3, "click"], ["updateButton", ""], [1, "far", "fa-save"], ["title", "Ta bort", 1, "delete-button", 3, "click"], [1, "fas", "fa-trash-alt"], [1, "day-admin-meals"], ["form", ""], ["type", "text", "placeholder", "Skriv ny matr\u00E4tt h\u00E4r...", 1, "new-meal-input-field", 3, "value"], ["newMealName", ""], ["title", "Vegetarisk", "type", "checkbox", "value", "veg", "name", "veg", "id", "veg", 1, "icon-checkbox"], ["title", "Stark", "type", "checkbox", "value", "hot", "name", "hot", "id", "hot", 1, "icon-checkbox"], ["title", "Fl\u00E4sk", "type", "checkbox", "value", "pig", "name", "pig", "id", "pig", 1, "icon-checkbox"], ["title", "Spara", 1, "save-button", 3, "click"], [1, "fas", "fa-save"]], template: function AdminMealsComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](0, "app-admin-header");
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵelement"](1, "app-header");
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵtemplate"](2, AdminMealsComponent_div_2_Template, 8, 1, "div", 0);
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_3__["ɵɵproperty"]("ngIf", ctx.week);
    } }, directives: [_admin_header_admin_header_component__WEBPACK_IMPORTED_MODULE_8__["AdminHeaderComponent"], _header_header_component__WEBPACK_IMPORTED_MODULE_9__["HeaderComponent"], _angular_common__WEBPACK_IMPORTED_MODULE_10__["NgIf"], _angular_common__WEBPACK_IMPORTED_MODULE_10__["NgForOf"], _angular_forms__WEBPACK_IMPORTED_MODULE_11__["ɵangular_packages_forms_forms_ba"], _angular_forms__WEBPACK_IMPORTED_MODULE_11__["NgControlStatusGroup"], _angular_forms__WEBPACK_IMPORTED_MODULE_11__["NgForm"]], pipes: [_angular_common__WEBPACK_IMPORTED_MODULE_10__["TitleCasePipe"], _angular_common__WEBPACK_IMPORTED_MODULE_10__["DatePipe"]], styles: ["@import url(\"https://use.fontawesome.com/releases/v5.14.0/css/all.css\");\n.centered-content[_ngcontent-%COMP%] {\n  height: 100%;\n}\n.menu-content[_ngcontent-%COMP%] {\n  margin-top: 55px;\n  max-width: 1000px;\n  display: flex;\n  flex-direction: column;\n  width: 100%;\n  height: 100%;\n  vertical-align: middle;\n  align-items: center;\n  justify-content: center;\n}\n.day-content[_ngcontent-%COMP%] {\n  background: #063249;\n  display: flex;\n  justify-content: left;\n  align-items: left;\n  max-width: 800px;\n  min-width: 800px;\n  max-height: 140px;\n  min-height: 140px;\n  border-radius: 8px;\n  margin: 3px;\n  padding: 20px;\n}\n.day-date[_ngcontent-%COMP%] {\n  background: #08496d;\n  display: flex;\n  max-width: 80px;\n  height: 90%;\n  justify-content: center;\n  align-items: center;\n  padding: 10px;\n  margin: 3px;\n  border-radius: 8px;\n  color: white;\n}\n.meals-content[_ngcontent-%COMP%] {\n  display: rows;\n  width: 100%;\n}\n.day-meals[_ngcontent-%COMP%], .day-admin-meals[_ngcontent-%COMP%] {\n  background: #08496d;\n  display: flex;\n  width: 100%;\n  height: 20%;\n  justify-content: left;\n  align-items: center;\n  margin: 5px;\n  border-radius: 8px;\n  color: white;\n}\n.icons[_ngcontent-%COMP%] {\n  display: flex;\n  align-items: center;\n  color: green;\n  margin-left: 3px;\n}\n.footer-content[_ngcontent-%COMP%] {\n  padding: 20px;\n}\n.new-meal-input-field[_ngcontent-%COMP%], .update-meal-input-field[_ngcontent-%COMP%] {\n  background: #08496d;\n  border: 0px;\n  border-radius: 8px;\n  color: white;\n  width: 480px;\n  height: 18px;\n  margin-bottom: 5px;\n  margin-left: 5px;\n}\n.icon-checkbox[_ngcontent-%COMP%] {\n  margin-top: 7px;\n  padding: 3px;\n  margin-left: 8px;\n  margin-right: 8px;\n  cursor: pointer;\n}\n.veg[_ngcontent-%COMP%] {\n  color: darkgreen;\n}\n.hot[_ngcontent-%COMP%] {\n  color: red;\n}\n.pig[_ngcontent-%COMP%] {\n  color: pink;\n}\n.delete-button[_ngcontent-%COMP%] {\n  cursor: pointer;\n  height: 16px;\n  width: 16px;\n  border-radius: 8px;\n  border: 0px;\n  background: transparent;\n  color: darkred;\n  margin-left: 3px;\n  margin-bottom: 5px;\n}\n.delete-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n.update-button[_ngcontent-%COMP%] {\n  cursor: pointer;\n  height: 16px;\n  width: 16px;\n  border-radius: 8px;\n  border: 0px;\n  background: transparent;\n  color: lightgreen;\n  margin-left: 15px;\n  margin-bottom: 5px;\n}\n.update-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n.save-button[_ngcontent-%COMP%] {\n  cursor: pointer;\n  height: 16px;\n  width: 16px;\n  border-radius: 8px;\n  border: 0px;\n  background: transparent;\n  color: lightgreen;\n  margin-left: 15px;\n  margin-bottom: 5px;\n}\n.save-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uL2FkbWluLW1lYWxzLmNvbXBvbmVudC5zY3NzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFRLHVFQUFBO0FBRVI7RUFDRSxZQUFBO0FBQUY7QUFHQTtFQUNFLGdCQUFBO0VBQ0EsaUJBQUE7RUFDQSxhQUFBO0VBQ0Esc0JBQUE7RUFDQSxXQUFBO0VBQ0EsWUFBQTtFQUNBLHNCQUFBO0VBQ0EsbUJBQUE7RUFDQSx1QkFBQTtBQUFGO0FBR0E7RUFDRSxtQkFBQTtFQUNBLGFBQUE7RUFDQSxxQkFBQTtFQUNBLGlCQUFBO0VBQ0EsZ0JBQUE7RUFDQSxnQkFBQTtFQUNBLGlCQUFBO0VBQ0EsaUJBQUE7RUFDQSxrQkFBQTtFQUNBLFdBQUE7RUFDQSxhQUFBO0FBQUY7QUFHQTtFQUNFLG1CQUFBO0VBQ0EsYUFBQTtFQUNBLGVBQUE7RUFDQSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxtQkFBQTtFQUNBLGFBQUE7RUFDQSxXQUFBO0VBQ0Esa0JBQUE7RUFDQSxZQUFBO0FBQUY7QUFHQTtFQUNFLGFBQUE7RUFDQSxXQUFBO0FBQUY7QUFHQTtFQUNFLG1CQUFBO0VBQ0EsYUFBQTtFQUNBLFdBQUE7RUFDQSxXQUFBO0VBQ0EscUJBQUE7RUFDQSxtQkFBQTtFQUNBLFdBQUE7RUFDQSxrQkFBQTtFQUNBLFlBQUE7QUFBRjtBQUdBO0VBQ0UsYUFBQTtFQUNBLG1CQUFBO0VBQ0EsWUFBQTtFQUNBLGdCQUFBO0FBQUY7QUFHQTtFQUNFLGFBQUE7QUFBRjtBQUdBO0VBQ0UsbUJBQUE7RUFDQSxXQUFBO0VBQ0Esa0JBQUE7RUFDQSxZQUFBO0VBQ0EsWUFBQTtFQUNBLFlBQUE7RUFDQSxrQkFBQTtFQUNBLGdCQUFBO0FBQUY7QUFHQTtFQUNFLGVBQUE7RUFDQSxZQUFBO0VBQ0EsZ0JBQUE7RUFDQSxpQkFBQTtFQUNBLGVBQUE7QUFBRjtBQUdBO0VBQ0UsZ0JBQUE7QUFBRjtBQUdBO0VBQ0UsVUFBQTtBQUFGO0FBR0E7RUFDRSxXQUFBO0FBQUY7QUFHQTtFQUNFLGVBQUE7RUFDQSxZQUFBO0VBQ0EsV0FBQTtFQUNBLGtCQUFBO0VBQ0EsV0FBQTtFQUNBLHVCQUFBO0VBQ0EsY0FBQTtFQUlBLGdCQUFBO0VBQ0Esa0JBQUE7QUFIRjtBQURFO0VBQ0UsMEJBQUE7QUFHSjtBQUdBO0VBQ0UsZUFBQTtFQUNBLFlBQUE7RUFDQSxXQUFBO0VBQ0Esa0JBQUE7RUFDQSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxpQkFBQTtFQUlBLGlCQUFBO0VBQ0Esa0JBQUE7QUFIRjtBQURFO0VBQ0UsMEJBQUE7QUFHSjtBQUdBO0VBQ0UsZUFBQTtFQUNBLFlBQUE7RUFDQSxXQUFBO0VBQ0Esa0JBQUE7RUFDQSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxpQkFBQTtFQUlBLGlCQUFBO0VBQ0Esa0JBQUE7QUFIRjtBQURFO0VBQ0UsMEJBQUE7QUFHSiIsImZpbGUiOiJhZG1pbi1tZWFscy5jb21wb25lbnQuc2NzcyIsInNvdXJjZXNDb250ZW50IjpbIkBpbXBvcnQgdXJsKFwiaHR0cHM6Ly91c2UuZm9udGF3ZXNvbWUuY29tL3JlbGVhc2VzL3Y1LjE0LjAvY3NzL2FsbC5jc3NcIik7XG5cbi5jZW50ZXJlZC1jb250ZW50IHtcbiAgaGVpZ2h0OiAxMDAlO1xufVxuXG4ubWVudS1jb250ZW50IHtcbiAgbWFyZ2luLXRvcDogNTVweDtcbiAgbWF4LXdpZHRoOiAxMDAwcHg7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIGZsZXgtZGlyZWN0aW9uOiBjb2x1bW47XG4gIHdpZHRoOiAxMDAlO1xuICBoZWlnaHQ6IDEwMCU7XG4gIHZlcnRpY2FsLWFsaWduOiBtaWRkbGU7XG4gIGFsaWduLWl0ZW1zOiBjZW50ZXI7XG4gIGp1c3RpZnktY29udGVudDogY2VudGVyO1xufVxuXG4uZGF5LWNvbnRlbnQge1xuICBiYWNrZ3JvdW5kOiAjMDYzMjQ5O1xuICBkaXNwbGF5OiBmbGV4O1xuICBqdXN0aWZ5LWNvbnRlbnQ6IGxlZnQ7XG4gIGFsaWduLWl0ZW1zOiBsZWZ0O1xuICBtYXgtd2lkdGg6IDgwMHB4O1xuICBtaW4td2lkdGg6IDgwMHB4O1xuICBtYXgtaGVpZ2h0OiAxNDBweDtcbiAgbWluLWhlaWdodDogMTQwcHg7XG4gIGJvcmRlci1yYWRpdXM6IDhweDtcbiAgbWFyZ2luOiAzcHg7XG4gIHBhZGRpbmc6IDIwcHg7XG59XG5cbi5kYXktZGF0ZSB7XG4gIGJhY2tncm91bmQ6ICMwODQ5NmQ7XG4gIGRpc3BsYXk6IGZsZXg7XG4gIG1heC13aWR0aDogODBweDtcbiAgaGVpZ2h0OiA5MCU7XG4gIGp1c3RpZnktY29udGVudDogY2VudGVyO1xuICBhbGlnbi1pdGVtczogY2VudGVyO1xuICBwYWRkaW5nOjEwcHg7XG4gIG1hcmdpbjozcHg7XG4gIGJvcmRlci1yYWRpdXM6IDhweDtcbiAgY29sb3I6IHdoaXRlO1xufVxuXG4ubWVhbHMtY29udGVudCB7XG4gIGRpc3BsYXk6IHJvd3M7XG4gIHdpZHRoOiAxMDAlO1xufVxuXG4uZGF5LW1lYWxzLCAuZGF5LWFkbWluLW1lYWxzIHtcbiAgYmFja2dyb3VuZDogIzA4NDk2ZDtcbiAgZGlzcGxheTogZmxleDtcbiAgd2lkdGg6IDEwMCU7XG4gIGhlaWdodDogMjAlO1xuICBqdXN0aWZ5LWNvbnRlbnQ6IGxlZnQ7XG4gIGFsaWduLWl0ZW1zOiBjZW50ZXI7XG4gIG1hcmdpbjogNXB4O1xuICBib3JkZXItcmFkaXVzOiA4cHg7XG4gIGNvbG9yOiB3aGl0ZTtcbn1cblxuLmljb25zIHtcbiAgZGlzcGxheTogZmxleDtcbiAgYWxpZ24taXRlbXM6IGNlbnRlcjtcbiAgY29sb3I6IGdyZWVuO1xuICBtYXJnaW4tbGVmdDogM3B4O1xufVxuXG4uZm9vdGVyLWNvbnRlbnQge1xuICBwYWRkaW5nOiAyMHB4O1xufVxuXG4ubmV3LW1lYWwtaW5wdXQtZmllbGQsIC51cGRhdGUtbWVhbC1pbnB1dC1maWVsZCB7XG4gIGJhY2tncm91bmQ6ICMwODQ5NmQ7XG4gIGJvcmRlcjogMHB4O1xuICBib3JkZXItcmFkaXVzOiA4cHg7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgd2lkdGg6IDQ4MHB4O1xuICBoZWlnaHQ6IDE4cHg7XG4gIG1hcmdpbi1ib3R0b206IDVweDtcbiAgbWFyZ2luLWxlZnQ6IDVweDtcbn1cblxuLmljb24tY2hlY2tib3gge1xuICBtYXJnaW4tdG9wOiA3cHg7XG4gIHBhZGRpbmc6IDNweDtcbiAgbWFyZ2luLWxlZnQ6IDhweDtcbiAgbWFyZ2luLXJpZ2h0OiA4cHg7XG4gIGN1cnNvcjogcG9pbnRlcjtcbn1cblxuLnZlZyB7XG4gIGNvbG9yOiBkYXJrZ3JlZW47XG59XG5cbi5ob3Qge1xuICBjb2xvcjogcmVkO1xufVxuXG4ucGlnIHtcbiAgY29sb3I6IHBpbms7XG59XG5cbi5kZWxldGUtYnV0dG9uIHtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICBoZWlnaHQ6IDE2cHg7XG4gIHdpZHRoOiAxNnB4O1xuICBib3JkZXItcmFkaXVzOiA4cHg7XG4gIGJvcmRlcjogMHB4O1xuICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgY29sb3I6IGRhcmtyZWQ7XG4gICY6YWN0aXZlIHtcbiAgICB0cmFuc2Zvcm06IHRyYW5zbGF0ZVkoNHB4KTtcbiAgfVxuICBtYXJnaW4tbGVmdDogM3B4O1xuICBtYXJnaW4tYm90dG9tOiA1cHg7XG59XG5cbi51cGRhdGUtYnV0dG9uIHtcbiAgY3Vyc29yOiBwb2ludGVyO1xuICBoZWlnaHQ6IDE2cHg7XG4gIHdpZHRoOiAxNnB4O1xuICBib3JkZXItcmFkaXVzOiA4cHg7XG4gIGJvcmRlcjogMHB4O1xuICBiYWNrZ3JvdW5kOiB0cmFuc3BhcmVudDtcbiAgY29sb3I6IGxpZ2h0Z3JlZW47XG4gICY6YWN0aXZlIHtcbiAgICB0cmFuc2Zvcm06IHRyYW5zbGF0ZVkoNHB4KTtcbiAgfVxuICBtYXJnaW4tbGVmdDogMTVweDtcbiAgbWFyZ2luLWJvdHRvbTogNXB4O1xufVxuXG4uc2F2ZS1idXR0b24ge1xuICBjdXJzb3I6IHBvaW50ZXI7XG4gIGhlaWdodDogMTZweDtcbiAgd2lkdGg6IDE2cHg7XG4gIGJvcmRlci1yYWRpdXM6IDhweDtcbiAgYm9yZGVyOiAwcHg7XG4gIGJhY2tncm91bmQ6IHRyYW5zcGFyZW50O1xuICBjb2xvcjogbGlnaHRncmVlbjtcbiAgJjphY3RpdmUge1xuICAgIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xuICB9XG4gIG1hcmdpbi1sZWZ0OiAxNXB4O1xuICBtYXJnaW4tYm90dG9tOiA1cHg7XG59XG5cblxuIl19 */"] });


/***/ }),

/***/ "zSnG":
/*!*****************************************************************************!*\
  !*** ./src/app/pages/admin/admin-user/delete-user/delete-user.component.ts ***!
  \*****************************************************************************/
/*! exports provided: DeleteUserComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "DeleteUserComponent", function() { return DeleteUserComponent; });
/* harmony import */ var src_app_models_user__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! src/app/models/user */ "2hxB");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var src_app_services_user_service__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! src/app/services/user.service */ "qfBg");
/* harmony import */ var src_assets_alert__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! src/assets/alert */ "X440");
/* harmony import */ var _auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @auth0/auth0-angular */ "2beD");
/* harmony import */ var _user_header_user_header_component__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! ../user-header/user-header.component */ "ySEZ");
/* harmony import */ var _angular_common__WEBPACK_IMPORTED_MODULE_6__ = __webpack_require__(/*! @angular/common */ "ofXK");







function DeleteUserComponent_a_7_Template(rf, ctx) { if (rf & 1) {
    const _r3 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵgetCurrentView"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](0, "a", 8);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵlistener"]("click", function DeleteUserComponent_a_7_Template_a_click_0_listener() { _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵrestoreView"](_r3); const user_r1 = ctx.$implicit; const ctx_r2 = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵnextContext"](); return ctx_r2.UserToDelete(user_r1); });
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](1, "p");
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
} if (rf & 2) {
    const user_r1 = ctx.$implicit;
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](2);
    _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtextInterpolate"](user_r1.email);
} }
class DeleteUserComponent {
    constructor(userService, alert, auth) {
        this.userService = userService;
        this.alert = alert;
        this.auth = auth;
        this.userToDeleteId = '';
        this.sub = [];
    }
    ngOnInit() {
        this.NameOfUserToDelete = "Välja användare att ta bort";
        this.$users = this.userService.getUsers();
        this.sub.push(this.auth.user$.subscribe((user) => {
            this.currentUser = new src_app_models_user__WEBPACK_IMPORTED_MODULE_0__["User"]();
            this.currentUser.setUserFromAuthPic(user.picture);
        }));
    }
    ngOnDestroy() {
        this.sub.forEach((sub) => {
            sub.unsubscribe();
        });
    }
    UserToDelete(user) {
        this.NameOfUserToDelete = user.email;
        this.userToDeleteId = user._id;
    }
    deleteUser() {
        if (!this.currentUser.permissions.some((permission) => permission === 'admin')) {
            this.alert.showAlert('', 'Du måste ha behörighet för att administrera användare!', 'error');
        }
        else {
            if (this.userToDeleteId === '') {
                this.alert.showAlert('', 'Du måste välja en användare att ta bort!', 'error');
            }
            else {
                this.alert.showAdvancedAlert('VARNING', 'Vill du ta bort denna användare?', 'warning', 'Ja, ta bort', 'Avbryt').then((result) => {
                    if (result.isConfirmed) {
                        this.sub.push(this.userService.deleteUser(this.userToDeleteId).subscribe(() => {
                        }, (err) => this.alert.showAlert('Nånting gick fel.', 'Användaren sparades inte', 'error'), () => this.alert.showAlertAndUpdatePage('Borttagen!', 'Användare har blivit borttagen.', 'success')));
                    }
                });
            }
        }
    }
}
DeleteUserComponent.ɵfac = function DeleteUserComponent_Factory(t) { return new (t || DeleteUserComponent)(_angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](src_app_services_user_service__WEBPACK_IMPORTED_MODULE_2__["UserService"]), _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](src_assets_alert__WEBPACK_IMPORTED_MODULE_3__["Alert"]), _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdirectiveInject"](_auth0_auth0_angular__WEBPACK_IMPORTED_MODULE_4__["AuthService"])); };
DeleteUserComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵdefineComponent"]({ type: DeleteUserComponent, selectors: [["app-delete-user"]], decls: 12, vars: 4, consts: [[1, "centered-content"], [1, "choices-content"], [1, "navbar-item", "has-dropdown", "is-hoverable"], [1, "navbar-link"], [1, "navbar-dropdown"], ["class", "navbar-item", 3, "click", 4, "ngFor", "ngForOf"], [1, "button-content"], [1, "delete-button", 3, "click"], [1, "navbar-item", 3, "click"]], template: function DeleteUserComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelement"](0, "app-user-header");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](1, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](2, "div", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](3, "div", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](4, "a", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](5);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](6, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtemplate"](7, DeleteUserComponent_a_7_Template, 3, 1, "a", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵpipe"](8, "async");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](9, "div", 6);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementStart"](10, "button", 7);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵlistener"]("click", function DeleteUserComponent_Template_button_click_10_listener() { return ctx.deleteUser(); });
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtext"](11, " Ta bort anv\u00E4ndare ");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵelementEnd"]();
    } if (rf & 2) {
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](5);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵtextInterpolate1"](" ", ctx.NameOfUserToDelete, " ");
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵadvance"](2);
        _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵproperty"]("ngForOf", _angular_core__WEBPACK_IMPORTED_MODULE_1__["ɵɵpipeBind1"](8, 2, ctx.$users));
    } }, directives: [_user_header_user_header_component__WEBPACK_IMPORTED_MODULE_5__["UserHeaderComponent"], _angular_common__WEBPACK_IMPORTED_MODULE_6__["NgForOf"]], pipes: [_angular_common__WEBPACK_IMPORTED_MODULE_6__["AsyncPipe"]], styles: [".choices-content[_ngcontent-%COMP%] {\n  vertical-align: middle;\n  align-items: center;\n  flex-direction: row;\n}\n\n.navbar-dropdown[_ngcontent-%COMP%] {\n  height: 200px;\n}\n\n.button-content[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: row;\n  align-items: center;\n  margin-left: 100px;\n}\n\n.delete-button[_ngcontent-%COMP%] {\n  width: 150px;\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n}\n\n.delete-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uLy4uLy4uL2RlbGV0ZS11c2VyLmNvbXBvbmVudC5zY3NzIl0sIm5hbWVzIjpbXSwibWFwcGluZ3MiOiJBQUFBO0VBQ0ksc0JBQUE7RUFDQSxtQkFBQTtFQUNBLG1CQUFBO0FBQ0o7O0FBRUE7RUFDSSxhQUFBO0FBQ0o7O0FBRUE7RUFDSSxhQUFBO0VBQ0EsbUJBQUE7RUFDQSxtQkFBQTtFQUNBLGtCQUFBO0FBQ0o7O0FBRUE7RUFDSSxZQUFBO0VBQ0EsV0FBQTtFQUNBLHVCQUFBO0VBQ0EsWUFBQTtFQUlBLGVBQUE7QUFGSjs7QUFESTtFQUNJLDBCQUFBO0FBR1IiLCJmaWxlIjoiZGVsZXRlLXVzZXIuY29tcG9uZW50LnNjc3MiLCJzb3VyY2VzQ29udGVudCI6WyIuY2hvaWNlcy1jb250ZW50IHtcbiAgICB2ZXJ0aWNhbC1hbGlnbjogbWlkZGxlO1xuICAgIGFsaWduLWl0ZW1zOiBjZW50ZXI7XG4gICAgZmxleC1kaXJlY3Rpb246IHJvdztcbn1cblxuLm5hdmJhci1kcm9wZG93biB7XG4gICAgaGVpZ2h0OiAyMDBweDtcbn1cblxuLmJ1dHRvbi1jb250ZW50IHtcbiAgICBkaXNwbGF5OiBmbGV4O1xuICAgIGZsZXgtZGlyZWN0aW9uOiByb3c7XG4gICAgYWxpZ24taXRlbXM6IGNlbnRlcjtcbiAgICBtYXJnaW4tbGVmdDogMTAwcHg7XG59XG5cbi5kZWxldGUtYnV0dG9uIHtcbiAgICB3aWR0aDogMTUwcHg7XG4gICAgYm9yZGVyOiAwcHg7XG4gICAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gICAgY29sb3I6IHdoaXRlO1xuICAgICY6YWN0aXZlIHtcbiAgICAgICAgdHJhbnNmb3JtOiB0cmFuc2xhdGVZKDRweCk7XG4gICAgfVxuICAgIGN1cnNvcjogcG9pbnRlcjtcbn1cbiJdfQ== */"] });


/***/ }),

/***/ "zUnb":
/*!*********************!*\
  !*** ./src/main.ts ***!
  \*********************/
/*! no exports provided */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _angular_platform_browser__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/platform-browser */ "jhN1");
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _app_app_module__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./app/app.module */ "ZAI4");
/* harmony import */ var _environments_environment__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! ./environments/environment */ "AytR");




if (_environments_environment__WEBPACK_IMPORTED_MODULE_3__["environment"].production) {
    Object(_angular_core__WEBPACK_IMPORTED_MODULE_1__["enableProdMode"])();
}
_angular_platform_browser__WEBPACK_IMPORTED_MODULE_0__["platformBrowser"]().bootstrapModule(_app_app_module__WEBPACK_IMPORTED_MODULE_2__["AppModule"])
    .catch(err => console.error(err));


/***/ }),

/***/ "zn8P":
/*!******************************************************!*\
  !*** ./$$_lazy_route_resource lazy namespace object ***!
  \******************************************************/
/*! no static exports found */
/***/ (function(module, exports) {

function webpackEmptyAsyncContext(req) {
	// Here Promise.resolve().then() is used instead of new Promise() to prevent
	// uncaught exception popping up in devtools
	return Promise.resolve().then(function() {
		var e = new Error("Cannot find module '" + req + "'");
		e.code = 'MODULE_NOT_FOUND';
		throw e;
	});
}
webpackEmptyAsyncContext.keys = function() { return []; };
webpackEmptyAsyncContext.resolve = webpackEmptyAsyncContext;
module.exports = webpackEmptyAsyncContext;
webpackEmptyAsyncContext.id = "zn8P";

/***/ }),

/***/ "zs3N":
/*!**************************************************************!*\
  !*** ./src/app/pages/login-failed/login-failed.component.ts ***!
  \**************************************************************/
/*! exports provided: LoginFailedComponent */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
__webpack_require__.r(__webpack_exports__);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LoginFailedComponent", function() { return LoginFailedComponent; });
/* harmony import */ var _angular_core__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! @angular/core */ "fXoL");
/* harmony import */ var _angular_forms__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! @angular/forms */ "3Pt+");
/* harmony import */ var _angular_router__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! @angular/router */ "tyNb");



class LoginFailedComponent {
    constructor() { }
    ngOnInit() {
    }
}
LoginFailedComponent.ɵfac = function LoginFailedComponent_Factory(t) { return new (t || LoginFailedComponent)(); };
LoginFailedComponent.ɵcmp = _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵdefineComponent"]({ type: LoginFailedComponent, selectors: [["app-login-failed"]], decls: 9, vars: 0, consts: [[1, "centered-content"], [1, "login-content"], [1, "fields"], [1, "logout-label"], [1, "buttons"], ["routerLink", "/", 1, "back-button"]], template: function LoginFailedComponent_Template(rf, ctx) { if (rf & 1) {
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](0, "div", 0);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](1, "div", 1);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](2, "form");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](3, "div", 2);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](4, "p", 3);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](5, " Inloggning misslyckades! ");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](6, "div", 4);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementStart"](7, "button", 5);
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵtext"](8, " Till startsidan ");
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
        _angular_core__WEBPACK_IMPORTED_MODULE_0__["ɵɵelementEnd"]();
    } }, directives: [_angular_forms__WEBPACK_IMPORTED_MODULE_1__["ɵangular_packages_forms_forms_ba"], _angular_forms__WEBPACK_IMPORTED_MODULE_1__["NgControlStatusGroup"], _angular_forms__WEBPACK_IMPORTED_MODULE_1__["NgForm"], _angular_router__WEBPACK_IMPORTED_MODULE_2__["RouterLink"]], styles: [".centered-content[_ngcontent-%COMP%] {\n  height: 100%;\n  vertical-align: center;\n}\n\n.login-content[_ngcontent-%COMP%] {\n  background: #063249;\n  display: flex;\n  justify-content: left;\n  align-items: left;\n  max-width: 360px;\n  min-width: 360px;\n  max-height: 160px;\n  min-height: 160px;\n  border-radius: 8px;\n  margin: 3px;\n  padding: 20px;\n}\n\n.fields[_ngcontent-%COMP%] {\n  display: flex;\n  flex-direction: column;\n}\n\n.back-button[_ngcontent-%COMP%] {\n  border: 0px;\n  background: transparent;\n  color: white;\n  cursor: pointer;\n  padding: 5px;\n  margin-left: 50px;\n  margin-top: 10px;\n}\n\n.back-button[_ngcontent-%COMP%]:active {\n  transform: translateY(4px);\n}\n\n.buttons[_ngcontent-%COMP%] {\n  margin-top: 10px;\n  margin-left: 160px;\n}\n\n.logout-label[_ngcontent-%COMP%] {\n  color: white;\n  margin-left: 20px;\n  margin-top: 30px;\n}\n/*# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIi4uLy4uLy4uLy4uL2xvZ2luLWZhaWxlZC5jb21wb25lbnQuc2NzcyJdLCJuYW1lcyI6W10sIm1hcHBpbmdzIjoiQUFBQTtFQUNFLFlBQUE7RUFDQSxzQkFBQTtBQUNGOztBQUVBO0VBQ0UsbUJBQUE7RUFDQSxhQUFBO0VBQ0EscUJBQUE7RUFDQSxpQkFBQTtFQUNBLGdCQUFBO0VBQ0EsZ0JBQUE7RUFDQSxpQkFBQTtFQUNBLGlCQUFBO0VBQ0Esa0JBQUE7RUFDQSxXQUFBO0VBQ0EsYUFBQTtBQUNGOztBQUVBO0VBQ0UsYUFBQTtFQUNBLHNCQUFBO0FBQ0Y7O0FBRUE7RUFDRSxXQUFBO0VBQ0EsdUJBQUE7RUFDQSxZQUFBO0VBSUEsZUFBQTtFQUNBLFlBQUE7RUFDQSxpQkFBQTtFQUNBLGdCQUFBO0FBRkY7O0FBSkU7RUFDRSwwQkFBQTtBQU1KOztBQUVBO0VBQ0UsZ0JBQUE7RUFDQSxrQkFBQTtBQUNGOztBQUVBO0VBQ0UsWUFBQTtFQUNBLGlCQUFBO0VBQ0EsZ0JBQUE7QUFDRiIsImZpbGUiOiJsb2dpbi1mYWlsZWQuY29tcG9uZW50LnNjc3MiLCJzb3VyY2VzQ29udGVudCI6WyIuY2VudGVyZWQtY29udGVudCB7XG4gIGhlaWdodDogMTAwJTtcbiAgdmVydGljYWwtYWxpZ246IGNlbnRlcjtcbn1cblxuLmxvZ2luLWNvbnRlbnQge1xuICBiYWNrZ3JvdW5kOiAjMDYzMjQ5O1xuICBkaXNwbGF5OiBmbGV4O1xuICBqdXN0aWZ5LWNvbnRlbnQ6IGxlZnQ7XG4gIGFsaWduLWl0ZW1zOiBsZWZ0O1xuICBtYXgtd2lkdGg6IDM2MHB4O1xuICBtaW4td2lkdGg6IDM2MHB4O1xuICBtYXgtaGVpZ2h0OiAxNjBweDtcbiAgbWluLWhlaWdodDogMTYwcHg7XG4gIGJvcmRlci1yYWRpdXM6IDhweDtcbiAgbWFyZ2luOiAzcHg7XG4gIHBhZGRpbmc6IDIwcHg7XG59XG5cbi5maWVsZHMge1xuICBkaXNwbGF5OiBmbGV4O1xuICBmbGV4LWRpcmVjdGlvbjogY29sdW1uO1xufVxuXG4uYmFjay1idXR0b24ge1xuICBib3JkZXI6IDBweDtcbiAgYmFja2dyb3VuZDogdHJhbnNwYXJlbnQ7XG4gIGNvbG9yOiB3aGl0ZTtcbiAgJjphY3RpdmUge1xuICAgIHRyYW5zZm9ybTogdHJhbnNsYXRlWSg0cHgpO1xuICB9XG4gIGN1cnNvcjogcG9pbnRlcjtcbiAgcGFkZGluZzogNXB4O1xuICBtYXJnaW4tbGVmdDogNTBweDtcbiAgbWFyZ2luLXRvcDogMTBweDtcbn1cblxuLmJ1dHRvbnMge1xuICBtYXJnaW4tdG9wOiAxMHB4O1xuICBtYXJnaW4tbGVmdDogMTYwcHg7XG59XG5cbi5sb2dvdXQtbGFiZWwge1xuICBjb2xvcjogd2hpdGU7XG4gIG1hcmdpbi1sZWZ0OiAyMHB4O1xuICBtYXJnaW4tdG9wOiAzMHB4O1xufVxuIl19 */"] });


/***/ })

},[[0,"runtime","vendor"]]]);
//# sourceMappingURL=main.js.map