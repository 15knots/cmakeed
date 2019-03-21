[Install...](#installation-instructions)
[![Build Status](https://travis-ci.org/15knots/cmakeed.svg?branch=master)](https://travis-ci.org/15knots/cmakeed)
[![GitHub issues](https://img.shields.io/github/issues/15knots/cmakeed.svg)](https://github.com/15knots/cmakeed/issues)

An Eclipse plug-in for editing CMake files such as CMakeLists.txt. Provides syntax coloring, CMake command content assist, 
and code templates.

Requires Java 8 or higher and Eclipse 4.5.2 or higher.

# Screenshots
Screenshots can be found at the <a href="https://marketplace.eclipse.org/content/cmake-editor#group-screenshots">Eclipse Marketplace</a>.

# Installation Instructions
The plugin may be installed
- from the [Eclipse Marketplace](https://marketplace.eclipse.org/content/cmake-editor#group-metrics-tab),
- by dragging this button [![Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client](https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.png)](http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=2983824 "Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client") into your running Eclipse workbench, or
- using the "Install New Software..." dialog and entering the update site URL listed below.

### Update Site
This composite update site contains the latest release as well as selected older releases: 
https://raw.githubusercontent.com/15knots/cmakeed/master/cmakeed-update/ .

Tool integrators will find each release at [bintray](https://bintray.com/15knots/p2-zip/cmakeed#files).
Each release is provided as a standalone zipped p2 repository and can be consumed in a PDE target platform. To add one
of these repositories to your target platform, add a **Software Site** and enter a URL for the location as
`jar:https://dl.bintray.com/15knots/p2-zip/CMakeEd-1.8.0.zip!/` (note the leading `jar:` and the trailing `!/`).

### Debug and Build
This project uses Apache maven as its build system.
To build from a command-line, run `mvn -f ./parent/pom.xml verify` in the root directory of the project source files.

There is a run configuration for eclipse to invoke maven `build cmakeed` plus a launch configuration to debug the plugin: `CmakeEd`.

---
# Release History

## 1.11.0 (2019-03-21)
### Changes
- Removed bundled cmake documentation (#8).
- Added matching bracket highlighting.
- Enhancement: Updated syntax highlighting data and tool tips to cmake 3.15.0.

## 1.10.0 (2018-11-22)
### Changes
- Enhancement: Updated syntax highlighting data and tool tips to cmake 3.13.0.

## 1.9.0 (2018-08-27)
### Changes
- Enhancement: Information text hover in the editor.
- Requires Eclipse 4.5.2 and Java 8 to run.

## 1.8.0 (2018-08-13)
### Changes
- Enhancement: Updated syntax highlighting data and tool tips to cmake 3.12.1.
- Fixed issue #4: Menu 'Edit | CMakeEd' is always visible. Removed comment actions from Edit menu.
Added 'Toggle Comment', 'Shift Left', 'Shift Right' actions to editor context menu incl. accelerators.
- Fixed issue #6: Multiline comment using bracket comment does not work.

## 1.7.0 (2018-03-31)
### Changes
- Enhancement: Updated syntax highlighting data and tool tips to cmake 3.11.0.

## 1.6.0 (2017-11-27)
### Changes
- Enhancement: Updated syntax highlighting data and tool tips to cmake 3.10.0.

## 1.5.0 (2017-10-31)
### Changes
- Project hosting moved from [Sourceforge](https://sourceforge.net/projects/cmakeed) to Github.
- Enhancement #1: Updated syntax highlighting data and tool tips to cmake 3.9.4.

## 1.4.0 (2017-07-29)
### Changes
- Update code completion for cmake commands to cmake version 3.5.2 (part of SF issue #7)
- Editor now respects the preference settings from General|Editors|Text Edtors preference page.

## 1.3.0 (2016-12-12)
### Changes
- Fixed issue #6: CMakeEd inserts always to the text 4 spaces by the tab pressing.
- Fixed feature #8: Allow to configure displayed tab width
- Code clean-up: Imports removed, classes parametrized, redundant null-checks removed

## 1.2.0 (2016-10-14)
### Changes
- New maintainer: Martin Weber.
- Build with maven and tycho.
- Binaries are hosted as zipped p2 repositories on [bintray.com](https://bintray.com/15knots/p2-zip/cmakeed) now.
- Newer version of CMake Editor will be detected by Eclipse`s 'Check for Updates' mechanism.
- Fixed issue #4: Undo History gets cleared on save.
- Fixed feature #7: also consider ctest files.

## 1.1.6 (2011-10-19)
### Changes
- Updated documentation for CMake 2.8.6

## 1.1.5 (2010-02-01)
### Changes
- Updated documentation for CMake 2.8.0
- Added preference to use either UPPERCASE or lowercase for CMake commands to better integrate with your current CMake files
- Cleaned up some parsing errors.

## 1.1.4 (2009-00-01)
### Changes
- Updated documentation for CMake 2.6.4

## 1.1.2 (2009-01-26)

## 1.1.1 (2009-01-19)
### Changes
- Syntax highlighting for CMake defined variables, User defined Variables, Properties and Reserved Words
- Preference Panel to set custom syntax highlighting colors
- Documentation updated to CMake version 2.6.2
- Support for keystroke code templates to insert often used cmake code
- Preference Panel to define custom keystroke code templates
- Keystroke short cuts to comment/uncomment blocks of code
- Basic hover support to show brief description of command or variable being hovered over.
- CMake command reference intregrated in the Eclipse Help system
- Editing of CMakeLists.txt and any *.cmake file or *.cmake.in file

## 1.1.0 (2008-12-29)

## 1.0.1 (2008-12-16)
New maintainer: M. Jackson.

## 1.0.0 (2007-04-21)
Initial version by Baron Roberts.
