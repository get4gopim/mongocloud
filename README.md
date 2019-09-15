# mongocloud
MongoDB Atlas Cloud Database

### REST API
- Get All Movies
- Save Movie
- Extract Movies from Wiki Page

## Get All Movies

<pre>
GET /demo/movies HTTP/1.1
Host: localhost:8080
X-Start-Year: 2010
X-End-Year: 2011
</pre>

##### Response
<pre>
[
    {
        "id": "5d7e02281c9d44000026ecf3",
        "title": "title",
        "type": "type",
        "actorName": "actorName",
        "actressName": "actressName",
        "musicDirector": "musicDirector",
        "flimDirector": "flimDirector",
        "imageUrl": "imageUrl",
        "releaseDate": 1234567890,
        "releaseYear": 2019,
        "language": "Tamil",
        "available": false
    },
    {
        "id": "5d7e0999df2f1800d89a8833",
        "title": "Captain Marvel",
        "type": null,
        "actorName": "Brie Larson",
        "actressName": "Samuel L. Jackson",
        "musicDirector": "Pinar Toprak",
        "flimDirector": "Anna Boden Ryan Fleck",
        "imageUrl": "https://upload.wikimedia.org/wikipedia/en/8/85/Captain_Marvel_poster.jpg",
        "releaseDate": 0,
        "releaseYear": 2019,
        "language": "English",
        "available": false
    }
]
</pre>

## Save Movie

##### Request
<pre>
POST /demo/movies HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
	
	"title": "title1",
	"type": "type1",
	"isAvailable": true,
	"actorName": "actorName1",
	"actressName": "actressName1",
	"musicDirector": "musicDirector1",
	"flimDirector": "flimDirector1",
	"releaseDate": 1234567890,
	"releaseYear": 1770,
	"language": "Tamil1",
	"imageUrl": "https://imageUrl1"
}
</pre>

##### Response

<pre>
{
    "id": "5d7e06bbdf2f1800d89a8832",
    "title": "title1",
    "type": "type1",
    "actorName": "actorName1",
    "actressName": "actressName1",
    "musicDirector": "musicDirector1",
    "flimDirector": "flimDirector1",
    "imageUrl": "https://imageUrl1",
    "releaseDate": 1234567890,
    "releaseYear": 1770,
    "language": "Tamil1",
    "available": false
}
</pre>


##### Error Response

<pre>
{
  "errorMessage": "Duplicate key entry error",
  "errorInfo": "E11000 duplicate key error collection: demo.doc_movies index: title_1_releaseYear_1 dup key: { : \"title1\", : 1770 }; nested exception is com.mongodb.MongoWriteException: E11000 duplicate key error collection: demo.doc_movies index: title_1_releaseYear_1 dup key: { : \"title1\", : 1770 }"
}
</pre>


## Extract Movies
This will fetch movie info from wiki page and insert into MongoDB cloud.

<pre>
GET /demo/movies/extract HTTP/1.1
Host: localhost:8080
X-Language: english
X-Start-Year: 2010
X-End-Year: 2016
</pre>

