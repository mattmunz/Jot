
app.controller('jotController', function($scope, $window, $http) {

  var ENTER_KEY_CODE = 13;
  
  var jotsURL = "../jots";

  $scope.author = 'Matt Munz';
  $scope.user = {firstName:'Matt', lastName:'Munz'};
  
  $scope.newJotText = "";

  $scope.createJot = function() {
  
    var successCallback = function(response) { /* TODO Log instead: alert("Add jot succeeded."); */ };
    var errorCallback = function(response) { 

      // TODO For some reason this alert shows even when the query succeeds!
      // alert("Add jot failed. Response: status: " + response.status + ", data: " + response.data); 
    };
    
    var newJot = { text: $scope.newJotText, time: new Date().toISOString() };
  
    $http.post(jotsURL, newJot).then(successCallback, errorCallback);
           
    $window.location.reload();
  };
  
  /* Creates a new jot when the user hits enter in the text box. */
  $scope.handleNewJotKeypress = function($event) {
  
    var keyCode = $event.which || $event.keyCode;

    if (keyCode === ENTER_KEY_CODE) { $scope.createJot(); }
  };

  $http.get(jotsURL).then(function(response) { $scope.jots = response.data; });
});
