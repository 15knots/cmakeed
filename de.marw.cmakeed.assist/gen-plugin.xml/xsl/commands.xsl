<?xml version="1.0" encoding="UTF-8"?>
<!--
  transforms documentation on cmake commands into extension point description code for CmakeEd
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output encoding="UTF-8" method="xml" media-type="text/xml" omit-xml-declaration="yes" />

  <xsl:template match="/">
    <xsl:if test="not(function-available('ends-with'))">
      <xsl:message terminate="yes" ><xsl:value-of select="'XPATH 2 is required to process this file!'" /></xsl:message>
    </xsl:if>

    <xsl:apply-templates />
  </xsl:template>

  <xsl:template match="/document/section">
    <xsl:variable name="name" select="title/text()" />
    <!-- assume the first paragraph element contains the description -->
    <xsl:variable name="descr" select="normalize-space(paragraph[1][text()])" />
    <!-- first word in description marks deprecated command -->
    <xsl:variable name="deprecated" select="starts-with($descr, 'Deprecated ') or starts-with($descr, 'Disallowed ')" />

    <!-- construct an element for cmakeed:
      < name=".." desc=".." deprecated=".." />
    -->
    <xsl:element name="command">
      <xsl:attribute name="name" select="$name" />
      <xsl:attribute name="desc" select="$descr" />

      <xsl:if test="$deprecated">
        <xsl:attribute name="deprecated" select="'true'" />
      </xsl:if>
      <!-- most commands use tag 'literal_block' for the usage, some use 'desc_name'... -->
      <xsl:choose>
        <xsl:when
          test="$name='cmake_file_api' or $name='cmake_language' or $name='cmake_policy'"
        >
           <xsl:apply-templates select="//desc_name">
            <xsl:with-param name="command" select="$name" tunnel="yes" />
          </xsl:apply-templates>
        </xsl:when>
        <xsl:when
          test="$name='file' or $name='if' or $name='install' or $name='list' or $name='set' or $name='string'"
        >
           <xsl:apply-templates select="//desc_name">
            <xsl:with-param name="command" select="$name" tunnel="yes" />
          </xsl:apply-templates>
        </xsl:when>
        <xsl:when
          test="$name='add_executable' or $name='add_library'"
        >
           <xsl:apply-templates select="//desc_name">
            <xsl:with-param name="command" select="$name" tunnel="yes" />
          </xsl:apply-templates>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates select="literal_block|section/section/literal_block">
            <xsl:with-param name="command" select="$name" tunnel="yes" />
          </xsl:apply-templates>
          <!-- for project command -->
          <xsl:apply-templates select="section[@ids='synopsis']/literal_block[@language='cmake']">
            <xsl:with-param name="command" select="$name" tunnel="yes" />
          </xsl:apply-templates>
        </xsl:otherwise>
      </xsl:choose>

      <xsl:apply-templates
        select="section[@ids!='synopsis' and not(contains(@ids,'example')) and @ids!='arguments' and @ids!='invocation' and @ids!='argument-caveats id2']/literal_block"
      >
        <xsl:with-param name="command" select="$name" tunnel="yes" />
      </xsl:apply-templates>
    </xsl:element>
    <xsl:value-of select="'&#10;'" />
  </xsl:template>

  <!-- literal_block in any section contains a usage description -->
  <xsl:template match="literal_block[@language='cmake' or @language='none' or not(@language)]">
    <xsl:param name="command" tunnel="yes" />
    <!-- <xsl:message terminate="no" >#<xsl:value-of select="text()" />#</xsl:message> -->
    <xsl:variable name="text" select="normalize-space(string-join(node()))" />
    <!-- remove command name from usage description -->
    <xsl:variable name="text2" select="normalize-space(substring-after($text, $command))" />

    <!--
      <xsl:message terminate="no"
      select="concat('DESC #',$text,'# : ',string(string-length($command)))" />
      <xsl:message terminate="no"
      select="concat('DESC #',substring-after($text,$command),'#')" />
    -->
    <xsl:choose>
      <!-- skip useless usages -->
      <xsl:when test="not(starts-with($text, $command))" />
      <xsl:when test="starts-with($text2,'(...')" />
      <xsl:when test="starts-with($text2,'(Experimental ')" />
      <xsl:when test="contains($text2,'myExe')" />
      <xsl:when test="contains($text2,'myexe')" />
      <xsl:when test="contains($text2,'mylib')" />
      <xsl:when test="contains($text2,'myTarget')" />
      <xsl:when test="starts-with($text2,'(foo')"/> 
      <xsl:when test="starts-with($text2,'(bar')"/> 
      <xsl:when test="starts-with($text,'if(var')"/>
      <!-- no longer needed as of cmake 3.26 
       <xsl:when test="$text2=''"/> 
        <xsl:when test="contains($text2,'libfoo')"/> <xsl:when test="contains($text2,'mypro')"/> -->
      <xsl:when test="$command='cmake_path'">
        <!-- CMAKE_PATH may have multiple usages in a single text block -->
        <xsl:for-each select="tokenize(text(), '&#10;')">
          <xsl:variable name="text" select="normalize-space(.)" />
          <xsl:if test="starts-with($text, $command)">
            <xsl:variable name="text2" select="normalize-space(substring-after($text, $command))" />
            <xsl:message terminate="no" >#<xsl:value-of select="$text" />#</xsl:message>
            <xsl:value-of select="'&#10;'" />
            <xsl:element name="usage">
              <xsl:attribute name="value" select="$text2" />
            </xsl:element>
            <xsl:value-of select="'&#10;'" />
          </xsl:if>
      </xsl:for-each>
    </xsl:when>
    <xsl:otherwise>
        <xsl:call-template name="write-usage">
            <xsl:with-param name="command" select="$command"/>
            <xsl:with-param name="usage" select="$text2"/>
        </xsl:call-template>
    </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- desc_name in any section contains a usage description -->
  <xsl:template match="desc_name">
    <xsl:param name="command" tunnel="yes" />
<!--     <xsl:message terminate="no" >#<xsl:value-of select="node()" />#</xsl:message> -->
    <xsl:variable name="text" select="normalize-space(string-join(node()))" />
    <!-- remove command name from usage description -->
    <xsl:variable name="usage" select="normalize-space(substring-after($text, $command))" />
    <!--
      <xsl:message terminate="no"
      select="concat('DESC #',$text,'# : ',string(string-length($command)))" />
      <xsl:message terminate="no"
      select="concat('DESC #',substring-after($text,$command),'#')" />
    -->
    <xsl:choose>
      <!-- skip useless usages -->
      <xsl:when test="not(starts-with($text, $command))" />
      <xsl:otherwise>
        <xsl:call-template name="write-usage">
            <xsl:with-param name="command" select="$command"/>
            <xsl:with-param name="usage" select="$usage"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="write-usage">
    <xsl:param name="command"/>
    <xsl:param name="usage"/>
      <xsl:message terminate="no" ><xsl:value-of select="$command"/> #<xsl:value-of select="$usage" />#</xsl:message>
      <xsl:value-of select="'&#10;'" />
      <xsl:element name="usage">
        <xsl:attribute name="value" select="$usage" />
      </xsl:element>
      <xsl:value-of select="'&#10;'" />
  </xsl:template>

  <!-- suppress printing of empty lines -->
  <xsl:template match="@*|node()">
    <xsl:apply-templates select="@*|node()" />
  </xsl:template>
</xsl:stylesheet>