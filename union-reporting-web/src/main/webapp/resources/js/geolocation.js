function geoFindMe() {

    if (!navigator.geolocation){
        return;
    }

    function success(position) {
        $('#addProject').modal('show')
    };

    function error() {
        console.log("error");
    };


    navigator.geolocation.getCurrentPosition(success, error);
}