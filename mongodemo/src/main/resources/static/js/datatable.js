$(document).ready( function () {
	 var table = $('#employeesTable').DataTable({
			"sAjaxSource": "/demo/movies",
			"sAjaxDataProp": "",
			"order": [[ 0, "asc" ]],
			"aoColumns": [
			    	{ "mData": "title"},
			    	{ "mData": "actorName" },
			    	{ "mData": "actressName" },
			    	{ "mData": "musicDirector" },
			    	{ "mData": "flimDirector" },
			    	{ "mData": "releaseYear" }
			]
	 })
});