<?xml version="1.0" encoding="UTF-8"?>
<!--
  transforms documentation on cmake command reserved words into extension point description code for
  CmakeEd
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output encoding="UTF-8" method="xml" media-type="text/xml" omit-xml-declaration="yes" />

  <!-- for testing purposes: output command name before reserved words from command -->
  <xsl:param name="printCommand" select="false()" />

  <!-- regex to match first character of a reserved word -->
  <xsl:variable name="rw-regex-0" select="'A-Z@'" />
  <!-- regex to match non-first char a reserved word -->
  <xsl:variable name="rw-regex" select="concat('[', $rw-regex-0, '][A-Z_]*[A-Z]')" />

  <xsl:template match="/">
    <xsl:if test="not(function-available('ends-with'))">
      <xsl:message terminate="yes" select="'XPATH 2 is required to process this file!'" />
    </xsl:if>
    <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="/document/section">
    <xsl:variable name="name" select="normalize-space(title/text())" />
    <xsl:if test="$printCommand">
      <xsl:comment select="$name" />
      <xsl:value-of select="'&#10;'" />
    </xsl:if>

    <xsl:apply-templates>
      <xsl:with-param name="command" select="$name" tunnel="yes" />
    </xsl:apply-templates>
    <xsl:value-of select="'&#10;'" />
  </xsl:template>

  <!-- Our best guess here is, that a reserved word is enclosed by a literal-tag. We exclude tags with
    attributes here, since they mostly are references to cmake vars or props. -->
  <xsl:template
    match="/document/section/paragraph//literal[not(@*)] | /document/section/definition_list//literal[not(@*)] |/document/section/section/paragraph//literal[not(@*)]">
    <xsl:param name="command" tunnel="yes" />
    <xsl:variable name="literal" select="normalize-space(text())" />

    <!-- pre-processing -->
    <xsl:choose>
      <!-- skip values that equal the command name -->
      <xsl:when test="$command=$literal" />
      <!-- blacklisting the usual suspects -->
      <xsl:when test="'True'=$literal" />
      <xsl:when test="'testDriver'=$literal" />
      <xsl:when test="'cmake_minimum_required(VERSION)'=$literal" />
      <xsl:when test="'cmake_policy(VERSION)'=$literal" />
      <xsl:when test="'cmake_policy(SET)'=$literal" />
      <xsl:when test="'sourceListName'=$literal" />
      <xsl:when test="'driverName'=$literal" />
      <xsl:when test="'Build.xml'=$literal" />
      <xsl:when test="'Configure.xml'=$literal" />
      <xsl:when test="'Coverage.xml'=$literal" />
      <xsl:when test="'MemCheck.xml'=$literal" />
      <xsl:when test="'Test.xml'=$literal" />
      <xsl:when test="'TestLoad'=$literal" />
      <xsl:when test="'Update.xml'=$literal" />
      <xsl:when test="'export(PACKAGE)'=$literal" />
      <xsl:when test="'A/b.h'=$literal" />
      <xsl:when test="'A.framework/Headers/b.h'=$literal" />
      <xsl:when test="'-F'=$literal" />
      <xsl:when test="'resultingLibraryName_FLTK_UI_SRCS'=$literal" />
      <xsl:when test="'VAR'=$literal" />
      <xsl:when test="'DestName'=$literal" />
      <xsl:when test="'SourceLists'=$literal" />
      <xsl:when test="'HeadersDestNamesource'=$literal" />
      <xsl:when test="'SourcesDestNamesource'=$literal" />
      <xsl:when test="'Foo::Bar'=$literal" />
      <xsl:when test="'A B A B'=$literal" />
      <xsl:when test="'@VAR@'=$literal" />
      <xsl:when test="'${VAR}'=$literal" />
      <xsl:when test="'N'=$literal" />
      <xsl:when test="'Debug'=$literal" />
      <xsl:when test="'&quot;OPTIONAL&quot;'=$literal" />
      <xsl:when test="'$ENV{PATH}'=$literal" />
      <xsl:when test="'(IMPORTED_)?LINK_INTERFACE_LIBRARIES(_&lt;CONFIG&gt;)?'=$literal" />
      <xsl:when test="'Find&lt;package&gt;.cmake'=$literal" />
      <xsl:when test="'CMP&lt;NNNN&gt;'=$literal" />
      <xsl:when test="''=$literal" />

      <!-- skip when leading 'CMakeLists' -->
      <xsl:when test="starts-with($literal, 'CMakeLists')" />
      <xsl:when test="starts-with($literal, '${CC}')" />
      <xsl:when test="starts-with($literal, 'my')" />
      <xsl:when test="starts-with($literal, 'MY_')" />
      <xsl:when test="starts-with($literal, '-')" />
      <xsl:when test="starts-with($literal, 'if(')" />
      <xsl:when test="starts-with($literal, '#cmakedefine')" />
      <xsl:when test="starts-with($literal, '#define')" />
      <xsl:when test="starts-with($literal, '#include')" />
      <xsl:when test="starts-with($literal, 'lib${')" />
      <xsl:when test="starts-with($literal, 'install(')" />
      <xsl:when test="starts-with($literal, 'foreach(')" />
      <xsl:when test="starts-with($literal, '${ARG')" />
      <!-- suppress build_name command, it is disallowed in cmake -->
      <xsl:when test="starts-with($literal, '${CMAKE_')" />
      <!-- suppress when trailing '_*' -->
      <xsl:when test="ends-with($literal, '_*')" />

      <xsl:otherwise>
        <xsl:call-template name="splice-leading">
          <xsl:with-param name="literal" select="normalize-space($literal)" />
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- split literal after leading garbage -->
  <xsl:template name="splice-leading">
    <xsl:param name="literal" />

    <!--
      <xsl:message terminate="no" select="concat('splice-leading: ',$literal)" />
    -->
    <xsl:choose>
      <!-- filters -->
      <xsl:when test="'&lt;prefix&gt;_UNPARSED_ARGUMENTS'=$literal" />
      <xsl:when test="'&lt;NNNN&gt;'=$literal" />
      <xsl:when test="'&lt;HTTP-header&gt;'=$literal" />
      <xsl:when test="'&lt;VAR&gt;-NOTFOUND'=$literal" />
      <xsl:when test="'&lt;package&gt;_CONFIG'=$literal" />
      <xsl:when test="'&lt;package&gt;_CONSIDERED_CONFIGS'=$literal" />
      <xsl:when test="'&lt;package&gt;_CONSIDERED_VERSIONS'=$literal" />
      <xsl:when test="'&lt;package&gt;_DIR'=$literal" />
      <xsl:when test="'&lt;package&gt;_VERSION'=$literal" />
      <xsl:when test="'&lt;package&gt;_VERSION_MAJOR'=$literal" />
      <xsl:when test="'&lt;package&gt;_VERSION_MINOR'=$literal" />
      <xsl:when test="'&lt;package&gt;_VERSION_PATCH'=$literal" />
      <xsl:when test="'&lt;package&gt;_VERSION_TWEAK'=$literal" />
      <xsl:when test="'&lt;package&gt;_VERSION_COUNT'=$literal" />
      <xsl:when test="'&lt;package&gt;_FIND_REQUIRED'=$literal" />
      <xsl:when test="'&lt;package&gt;_FIND_QUIETLY'=$literal" />
      <xsl:when test="'&lt;package&gt;_FIND_VERSION'=$literal" />
      <xsl:when test="'&lt;package&gt;_FIND_VERSION_MAJOR'=$literal" />
      <xsl:when test="'&lt;package&gt;_FIND_VERSION_MINOR'=$literal" />
      <xsl:when test="'&lt;package&gt;_FIND_VERSION_PATCH'=$literal" />
      <xsl:when test="'&lt;package&gt;_FIND_VERSION_TWEAK'=$literal" />
      <xsl:when test="'&lt;package&gt;_FIND_VERSION_COUNT'=$literal" />
      <xsl:when test="'&lt;package&gt;_FIND_VERSION_EXACT'=$literal" />
      <xsl:when test="'&lt;package&gt;_FIND_COMPONENTS'=$literal" />
      <xsl:when test="'&lt;package&gt;_FIND_REQUIRED_&lt;c&gt;'=$literal" />
      <xsl:when test="'&lt;package&gt;_FOUND'=$literal" />
      <xsl:when test="'&lt;bindir&gt;/CMakeFiles/CMakeTmp'=$literal" />
      <xsl:when test="'&lt;LANG&gt;_STANDARD'=$literal" />
      <xsl:when test="'&lt;LANG&gt;_STANDARD_REQUIRED'=$literal" />
      <xsl:when test="'&gt;_STANDARD_REQUIRED'=$literal" />
      <xsl:when test="'&lt;LANG&gt;_EXTENSIONS'=$literal" />
      <xsl:when test="''=$literal" />

      <xsl:otherwise>
        <xsl:analyze-string regex="(.*?)({$rw-regex})(.*)"
          select="$literal">
          <xsl:matching-substring>
            <!--
            <xsl:message terminate="no" select="concat('MATCH splice-leading: #', $literal, '#')" />
            -->
            <!-- output reserved word -->
            <xsl:call-template name="filter-reservedword">
              <xsl:with-param name="literal" select="regex-group(2)" />
            </xsl:call-template>
            <!-- recurse with trailing garbage -->
            <xsl:call-template name="splice-leading">
              <xsl:with-param name="literal" select="normalize-space(regex-group(3))" />
            </xsl:call-template>
          </xsl:matching-substring>
        </xsl:analyze-string>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="filter-reservedword">
    <xsl:param name="literal" />

    <xsl:choose>
      <!-- final filters, add as needed -->
      <xsl:when test="''=$literal" />

      <xsl:otherwise>
        <xsl:call-template name="reservedword">
          <xsl:with-param name="word" select="$literal" />
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- outputs the element -->
  <xsl:template name="reservedword">
    <xsl:param name="word" />

    <xsl:message terminate="no" select="concat('reservedword: ', $word)" />
    <!--
    -->
    <xsl:element name="reservedword">
      <xsl:attribute name="name" select="$word" />
    </xsl:element>
    <xsl:value-of select="'&#10;'" />
  </xsl:template>

  <!-- suppress 'build_name' command, it is disallowed -->
  <xsl:template match="/document/section/title[text()='build_name']"/>

  <!-- suppress printing of empty lines -->
  <xsl:template match="@*|node()">
    <xsl:apply-templates select="@*|node()" />
  </xsl:template>
</xsl:stylesheet>