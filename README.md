# Clothesline Compatibility layer for Scala

This library is intended to provide helpers to make working
with Clothesline projects from Scala simpler and more idiomatic

Currently Clothesline does not have a Maven repository, but you
can fake it by following these steps from the Clothesline project
directory:

    M2=$HOME/.m2/repository/Clothesline/Clothesline/1.0.0-SNAPSHOT/

    lein clean
    lein jar
    lein pom

    mkdir -p $M2
    cp Clothesline-1.0.0-SNAPSHOT.jar $M2/
    cp pom.xml $M2/Clothesline-1.0.0-SNAPSHOT.pom
    rm -r ~/.ivy2/cache/Clothesline
    

This project is already set up to use your local Maven repository,
so at this point, running 'sbt update' should work correctly.
