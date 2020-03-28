# How to generate CmakeEd`s code completion data from cmake source code

For the rest of this document `$CMAKESRC` refers to the location of the cmake sources.

## Preparation
- Make sure, package `python-Sphinx` (if on openSUSE) is installed:
 - `sudo zypper install --no-recommends  python-Sphinx` 
- Make sure, `ant` is installed, it is needed to run the transformations.
- Make sure, an XPATH 2 compatible XSLT transformer is in stalled, currently saxon9.
 - On openSUSE run `sudo zypper in saxon9`.
- Checkout the cmake sources.
 1. Clone the cmake sources to `$CMAKESRC`: Run `git clone https://gitlab.kitware.com/cmake/cmake.git`.
 - Switch to the version tag you want.

- Generate build scripts for cmake with cmake in the build directory, telling it to build documentation.
 1. Create a build directory: `mkdir -p $CMAKESRC/build`.
 - Run `(cd $CMAKESRC/build && cmake -DSPHINX_HTML=ON ..)`.
- Generate XML files from the sources in `$CMAKESRC/Utilities/Sphinx/doctree`
into directory `$CMAKESRC/build/xml/`.
 - Generate cmake documentation in XML format:
 `sphinx-build -c $CMAKESRC/build/Utilities/Sphinx -b xml $CMAKESRC/Help $CMAKESRC/build/xml`

One-liner to the the two steps above:
export CMAKESRC=~/devel/fremd/cmake && (mkdir $CMAKESRC/build; cd $CMAKESRC/build && cmake -DSPHINX_HTML=ON -DCTEST_TEST_CPACK=OFF -DCTEST_TEST_CPACK=OFF -DCMAKE_RUN_LONG_TESTS=OFF -DCMAKE_USE_OPENSSL=OFF ..) && sphinx-build -c $CMAKESRC/build/Utilities/Sphinx -b xml $CMAKESRC/Help $CMAKESRC/build/xml

## Generate an updated `plugin.xml` file
- Run the ant script `gen-plugin_xml.ant.xml` with ant: `ant -Dcmake.src.root=$CMAKESRC` or use the eclipse launch definition 
 if you use Eclipse. This will create a new file `plugin.xml` unte project root directory.
- Update the CMake version number in file `plugin.properties` to the version tag you selected earlier.