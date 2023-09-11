export class MockConfigService {
    config = {
        layers:[],
        backgroundColor: null,
        projectionCode:""
    };
    getInitialLocation() {
        return {x: 0, y: 0, zoom: 0}
    };
}