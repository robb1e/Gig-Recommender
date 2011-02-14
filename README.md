Gig Recommender
===============

This project is very much a hack and needs some love.

Concept
-------

This takes a music festival band list and builds a dataset based on artists that are similar to those playing.
For SXSW, over a thousand bands mean a dataset of around 20,000 similar artists.  Then using a given username from a
Last.FM profile, the top artists are checked against those similar artists to find bands playing that a user might
be interested in, but might not have heard of, or might not know are playing due to the huge event.

Requirements
------------

This project is built using Scala, uses SBT for dependency management and MongoDB for storage.