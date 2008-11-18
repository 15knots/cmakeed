#!/bin/bash
# This is a shell script to generate a QtAssistant set of docs from the CMake distribution.
#
# Couple of things to get you started with this file:function file
# 1. Set the CMAKE variable to the cmake executable for the CMAKE cvs if it is not
# the default on your system
# 2. I change the definition of CMAKE to 'cmake' when I generate the "module" list because
# the current CMake cvs seg faults when I use the cmake --help-module-list command
# 3. All the files are generated in the /tmp directory. If you want them somewhere else 
# set the 'generationDir' variable
# 4. Launch QtAssistant with the -profile argument, for example on OS X I use
# ./assistant.app/Contents/MacOS/assistant -profile /private/tmp/cmake_assistant_docs/CMakeDocs.adp 
# where my pwd is: ${QTDIR}/bin or
# /Users/Shared/Toolkits/Qt-4.3.5-UBLib/bin
# Change the 'qtassistant' variable to point to your QtAssistant executable

#export QTDIR="/Users/Shared/Toolkits/Qt-4.3.5-UBDylib"
#qtassistant="$QTDIR/bin/Assistant_adp.app/Contents/MacOS/assistant_adp"
CMAKE="/Users/Shared/Toolkits/CMake-2.6.2/cmake"

generationDir="./"
assistantDir="${generationDir}/doc"
# remove any previous builds of the docs
rm -rf ${assistantDir}
mkdir -p ${assistantDir}

tocFile=${generationDir}/toc.xml
tmpHTMLFile="${assistantDir}/tmp.html"

#-- Create a shell script to launch QtAssistant once everything is built
#echo "${QTDIR}/bin/Assistant_adp.app/Contents/MacOS/assistant_adp -profile /tmp/cmake_assistant_docs/CMakeDocs.adp" > /tmp/cmakedocs.sh
#chmod ugo+rwx /tmp/cmakedocs.sh
#css_sheet="http://www.kitware.com/kitware.css"
css_sheet="cmake.css"

#----------------------------------------------------------------
# Start the main index.html file that lists the major doc groups, Commands, Modules, Variables
#----------------------------------------------------------------
mainIndexFileName="index.html"
mainIndexFile=${assistantDir}/${mainIndexFileName}
echo "" > ${mainIndexFile}  
echo "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"" >> ${mainIndexFile}
echo "\"http://www.w3.org/TR/html4/loose.dtd\">" >> ${mainIndexFile}
echo "<html><head>" >> ${mainIndexFile}
echo "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> " >> ${mainIndexFile}
echo "<title>CMake Documentation</title>" >> ${mainIndexFile}
echo "<link href=\"$css_sheet\" rel=\"stylesheet\" type=\"text/css\" />" >> ${mainIndexFile}
echo "</head>" >> ${mainIndexFile}
echo "<body>" >> ${mainIndexFile}
echo "<div id=\"ContentTxtProd\">" >> ${mainIndexFile}

#----------------------------------------------------------------
# Start the eclipse index.html file
#----------------------------------------------------------------
echo "" > $tocFile
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" > $tocFile
echo "<?NLS TYPE=\"org.eclipse.help.toc\"?>" >> $tocFile
echo "<toc label=\"CMake Documentation\">" >> $tocFile


#################################################################
# Function GenerateDocsForCommand This function will create all the
# individual html files, an index file and the proper section of the .adp file
# $1 The type of docs we are going to generate 'command' 'module' 'variable'
function GenerateDocsForCommand()
{
  docDirName="cmake_${1}_docs"
  docDir="${assistantDir}/${docDirName}"
  mkdir ${docDir}
  listFile="${generationDir}/list.txt"
  ${CMAKE} --help-${1}-list ${listFile}
  echo "* Generating HTML files for command '${1}'"
  #----------------------------------------------------------------
  # Start the Index File for this group of docs
  #----------------------------------------------------------------
  indexFile=${docDir}/cmake_${1}_index.html
  relIndexFile="doc/$docDirName/cmake_${1}_index.html"
  echo "" > $indexFile  
  echo "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"" >> $indexFile
  echo "\"http://www.w3.org/TR/html4/loose.dtd\">" >> $indexFile
  echo "<html><head>" >> $indexFile
  echo "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> " >> $indexFile
  echo "<title>CMake ${1} Index</title>" >> $indexFile
  echo "<link href=\"../$css_sheet\" rel=\"stylesheet\" type=\"text/css\" />" >> $indexFile
  echo "</head>" >> $indexFile
  echo "<body>" >> $indexFile
  echo "<div id=\"ContentTxtProd\">" >> $indexFile
  echo "<p><a href=\"../index.html\">All CMake Documentation</a></p>" >> $indexFile
  
  #----------------------------------------------------------------
  # Create a section in the toc.xml file
  #----------------------------------------------------------------  
  echo "      <topic label=\"CMake ${1}s\" href=\"${relIndexFile}\">" >> $tocFile
  
  #----------------------------------------------------------------
  # Create a section in the top level index file
  #----------------------------------------------------------------
  echo "<p><a target=\"DocFrame\" href=\"./${docDirName}/cmake_${1}_index.html\">CMake ${1}s</a></p>" >> ${mainIndexFile}
  
  #----------------------------------------------------------------
  # Generate all the individual documentation html files
  #----------------------------------------------------------------
  i=0
  exec 9<${listFile}
  while read -u 9 line
    do
    if [[ ${i} -gt 0 ]]; then
      echo ${i}": ${line}"
      # Seems that CMake only puts out the body portion of the html
      ${CMAKE} --help-${1} "${line}" ${tmpHTMLFile}
      sub=`echo ${line} | sed 's/</\&lt;/g' |  sed 's/>/\&gt;/g'`
      htmlFile=`echo ${line} | tr '[' '-' | tr ']' '-' | tr '>' '-' | tr '<' '-' | tr ' ' '_'`
      relHtmlFile="doc/$docDirName/$htmlFile.html"
      htmlFile=${docDir}/${htmlFile}.html
      # Clear the file in case it is left over from previous run
      echo "" > ${htmlFile}  
      echo "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"" >> ${htmlFile}
      echo "\"http://www.w3.org/TR/html4/loose.dtd\">" >> ${htmlFile}
      echo "<html><head>" >> ${htmlFile}
      echo "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> " >> ${htmlFile}
      echo "<title>CMake ${1} ${line}</title>" >> ${htmlFile}
      echo "<link href=\"../$css_sheet\" rel=\"stylesheet\" type=\"text/css\" />" >> ${htmlFile}
      echo "</head>" >> ${htmlFile}
      echo "<body>" >> ${htmlFile}
      echo "<div id=\"ContentTxtProd\">" >> ${htmlFile}
      echo "<p><a href=\"./cmake_${1}_index.html\">All CMake ${1}s</a></p>" >> ${htmlFile}
      cat ${tmpHTMLFile} >> ${htmlFile}
      echo "</div></body></html>" >> ${htmlFile}
      
      # Add a line to the index file
      echo "<p><a href=\"${line}.html\">${line}</a></p>" >> $indexFile
      
      # Add the keyword and section for this piece of documentation to the toc.xml file
      echo "        <topic label=\"${sub}\" href=\"${relHtmlFile}\" />" >> $tocFile
      
    fi
    let i=i+1
  done
  exec 9<&-
  
  # finish the html code in the index file
  echo "</div></body></html>" >> $indexFile
  
  # Finish the section in the .adp file
  echo "   </topic>" >> $tocFile
  
  #----------------------------------------------------------------
  # Remove Temp Files
  #----------------------------------------------------------------
  rm $tmpHTMLFile
  rm $listFile
}
#################################################################

#################################################################
function GenerateDocsForSection()
{
  docDirName="cmake_${1}_docs"
  docDir="${assistantDir}/${docDirName}"
  mkdir ${docDir}
  indexFile="${docDir}/cmake_${1}_index.html"
  relIndexFile="doc/$docDirName/cmake_${1}_index.html"
  ${CMAKE} --help-${1} $indexFile
  echo "* Generating HTML files for section '${1}'"
  
  #----------------------------------------------------------------
  # Create a section in the .adp file
  #----------------------------------------------------------------  
  echo "  <topic label=\"CMake ${1}\" href=\"${relIndexFile}\" >" >> $tocFile
  
  #----------------------------------------------------------------
  # Create a section in the top level index file
  #----------------------------------------------------------------
  echo "<p><a target=\"DocFrame\" href=\"./${docDirName}/cmake_${1}_index.html\">CMake ${1}s</a></p>" >> ${mainIndexFile}

  # Finish the section in the .adp file
  echo "  </topic>" >> $tocFile
  
}
#################################################################

function GenerateCopyRightHTML()
{
  docDirName="cmake_${1}_docs"
  docDir="${assistantDir}/${docDirName}"
  mkdir ${docDir}
  listFile="${generationDir}/list.txt"
  ${CMAKE} --${1} ${listFile}
  echo "* Generating HTML files for command '${1}'"
  #----------------------------------------------------------------
  # Start the Index File for this group of docs
  #----------------------------------------------------------------
  indexFile=${docDir}/cmake_${1}_index.html
  echo "" > $indexFile  
  echo "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"" >> $indexFile
  echo "\"http://www.w3.org/TR/html4/loose.dtd\">" >> $indexFile
  echo "<html><head>" >> $indexFile
  echo "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> " >> $indexFile
  echo "<title>CMake ${1} Index</title>" >> $indexFile
  echo "<link href=\"../$css_sheet\" rel=\"stylesheet\" type=\"text/css\" />" >> $indexFile
  echo "</head>" >> $indexFile
  echo "<body>" >> $indexFile
  echo "<div id=\"ContentTxtProd\">" >> $indexFile
  echo "<p><a href=\"../index.html\">All CMake Documentation</a></p>" >> $indexFile
  
  echo "<code style=\"display: block;\">" >> $indexFile
  #-- Write the body
  i=0
  exec 9<${listFile}
  while read -u 9 line
    do
      echo "$line" >> $indexFile  
  done
  exec 9<&-
  # finish the html code in the index file
  echo "</code></div></body></html>" >> $indexFile
  
  #----------------------------------------------------------------
  # Create a section in the .adp file
  #----------------------------------------------------------------  
  echo "      <section ref=\"$indexFile\" title=\"CMake ${1}s\">" >> $tocFile
  # Finish the section in the .adp file
  echo "   </section>" >> $tocFile
  
  #----------------------------------------------------------------
  # Create a section in the top level index file
  #----------------------------------------------------------------
  echo "<p><a target=\"DocFrame\" href=\"./${docDirName}/cmake_${1}_index.html\">CMake ${1}s</a></p>" >> ${mainIndexFile}

  #----------------------------------------------------------------
  # Remove Temp Files
  #----------------------------------------------------------------
  rm $listFile
}
#################################################################

GenerateDocsForCommand "command"
GenerateDocsForCommand "module"
GenerateDocsForCommand "variable"
GenerateDocsForCommand "property"
GenerateDocsForSection "policies"
GenerateDocsForSection "compatcommands"

#GenerateCopyRightHTML "copyright"

#----------------------------------------------------------------
# Finish the top level index File
#----------------------------------------------------------------
echo "</div>" >> ${mainIndexFile}
echo "</body>" >> ${mainIndexFile}
echo "</html>" >> ${mainIndexFile}


#----------------------------------------------------------------
# Finish the toc.xml File
#----------------------------------------------------------------
echo "</toc>" >> $tocFile


#----------------------------------------------------------------
# Create a frame.html file so normal web browsers can use this also
#----------------------------------------------------------------
mainIndexFileName="frame.html"
mainIndexFile=${assistantDir}/${mainIndexFileName}
echo "" > ${mainIndexFile}  
echo "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"" >> ${mainIndexFile}
echo "\"http://www.w3.org/TR/html4/loose.dtd\">" >> ${mainIndexFile}
echo "<html><head>" >> ${mainIndexFile}
echo "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"> " >> ${mainIndexFile}
echo "<title>CMake Documentation Frame Version</title>" >> ${mainIndexFile}
echo "<link href=\"$css_sheet\" rel=\"stylesheet\" type=\"text/css\" />" >> ${mainIndexFile}
echo "</head>" >> ${mainIndexFile}
echo "<body>" >> ${mainIndexFile}

echo "<frameset cols="25%,75%">" >> ${mainIndexFile}
echo "<frame src=\"index.html\">" >> ${mainIndexFile}
echo "<frame name=\"DocFrame\" src=\"cmake_command_docs/cmake_command_index.html\">" >> ${mainIndexFile}
echo "</frameset>" >> ${mainIndexFile}

echo "</body>" >> ${mainIndexFile}
echo "</html>" >> ${mainIndexFile}

#----------------------------------------------------------------
# Create a CSS sheet to style some of the text
#----------------------------------------------------------------
mainIndexFileName="${css_sheet}"
mainIndexFile=${assistantDir}/${mainIndexFileName}
echo "" > ${mainIndexFile}
echo "body {" >> ${mainIndexFile}
echo "	background : #FFFFFF;" >> ${mainIndexFile}
echo "	font-family:'Lucida Grande';" >> ${mainIndexFile}
echo "}" >> ${mainIndexFile}
echo " ul" >> ${mainIndexFile}
echo " {" >> ${mainIndexFile}
echo "   border: solid 1px #555555;" >> ${mainIndexFile}
echo " }" >> ${mainIndexFile}
echo "" >> ${mainIndexFile}
echo " A:link			{ COLOR: #0000FF; TEXT-DECORATION: none; }" >> ${mainIndexFile}
echo " A:link:hover	{ COLOR: #FF6600; TEXT-DECORATION: underline }" >> ${mainIndexFile}
echo " A:active		{ COLOR: #FF6600; TEXT-DECORATION: underline}" >> ${mainIndexFile}
echo "" >> ${mainIndexFile}
echo " A:visited		{ COLOR: #0000AA; TEXT-DECORATION: none }" >> ${mainIndexFile}
echo " A:visited:hover	{ COLOR: #FF6600; TEXT-DECORATION: underline } " >> ${mainIndexFile}
echo " A.resources:link			{ COLOR: #0000FF; TEXT-DECORATION: none }" >> ${mainIndexFile}
echo " A.resources:link:hover		{ COLOR: #ff6600; TEXT-DECORATION: underline }" >> ${mainIndexFile}
echo " A.resources:visited			{ COLOR: #0000FF; TEXT-DECORATION: none }" >> ${mainIndexFile}
echo " A.resources:visited:hover	{ COLOR: #ff6600; TEXT-DECORATION: underline }" >> ${mainIndexFile}
echo " A.resources:active			{ COLOR: #ff6600; TEXT-DECORATION: underline }" >> ${mainIndexFile}
echo "" >> ${mainIndexFile}
echo " .aLink {" >> ${mainIndexFile}
echo " 	color: black;" >> ${mainIndexFile}
echo " 	font: 80% Verdana, Arial, Helvetica, sans-serif;" >> ${mainIndexFile}
echo " 	text-decoration: none;" >> ${mainIndexFile}
echo " 	text-align: left;" >> ${mainIndexFile}
echo " 	border-right: 1px solid black;" >> ${mainIndexFile}
echo " 	border-left: 1px solid black;" >> ${mainIndexFile}
echo " 	background: white;" >> ${mainIndexFile}
echo " 	padding-top: 1px;" >> ${mainIndexFile}
echo " 	padding-bottom: 1px;" >> ${mainIndexFile}
echo " 	padding-right: 5px;" >> ${mainIndexFile}
echo " 	padding-left: 5px;" >> ${mainIndexFile}
echo " 	vertical-align:top;" >> ${mainIndexFile}
echo " 	width:150px;" >> ${mainIndexFile}
echo " }" >> ${mainIndexFile}
echo "" >> ${mainIndexFile}



#----------------------------------------------------------------
# Launch QtAssistant with the proper arguments
# echo "${qtassistant} -profile $tocFile"
#${qtassistant} -profile $tocFile &

