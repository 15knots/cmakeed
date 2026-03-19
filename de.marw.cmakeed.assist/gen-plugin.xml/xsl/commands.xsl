<?xml version="1.0" encoding="UTF-8"?>
<!--
  transforms documentation on cmake commands into extension point description code for CmakeEd
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output encoding="UTF-8" method="xml" media-type="text/xml" omit-xml-declaration="yes" />

  <xsl:param name="process-reswords"  select="false()"/>

  <xsl:template match="/">
    <xsl:if test="not(function-available('ends-with'))">
      <xsl:message terminate="yes" ><xsl:value-of select="'XPATH 2 is required to process this file!'" /></xsl:message>
    </xsl:if>

    <xsl:apply-templates />
  </xsl:template>

  <!--  Most commands use XPATH '/document/section/literal_block[@language='cmake']' for the signature -->
  <!-- commands with usage in XPATH '/document/section/desc/desc_signature/desc_name'.  -->
  <xsl:variable name="usageIn_desc_name">|cmake_file_api|create_test_sourcelist|load_cache|</xsl:variable>
  <!-- commands with usage in XPATH '/document/section/section/desc/desc_signature/desc_name' -->
  <xsl:variable name="usageIn_section_desc_name">|cmake_host_system_information|add_executable|add_library|cmake_policy|set|unset|</xsl:variable>
  <!-- commands with preferred usage in XPATH '/document/section/section/section/desc/desc_signature/desc_name'.  -->
  <xsl:variable name="preferUsageIn_desc_name">|add_custom_command|if|string|</xsl:variable>
  <!-- commands with multiple signatures in a single XMl element; one per line -->
  <xsl:variable name="multiUsagesIn_element">|cmake_language|cmake_path|file|install|list|source_group|</xsl:variable>

  <xsl:template match="/document/section">
    <xsl:variable name="command" select="title/text()" />
    <xsl:choose>
      <xsl:when test="$process-reswords">
        <!-- get command signature(s)... -->
        <xsl:call-template name="process-signatures">
          <xsl:with-param name="command" select="$command" tunnel="yes" />
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <!-- assume the first paragraph element contains the description -->
        <xsl:variable name="descr" select="normalize-space(paragraph[1][text()])" />
        <!-- first word in description marks deprecated command -->
        <xsl:variable name="deprecated"
          select="starts-with($descr, 'Deprecated ') or starts-with($descr, 'Disallowed ')" />
        <!-- construct an element for cmakeed: < name=".." desc=".." deprecated=".." /> -->
        <xsl:element name="command">
          <xsl:attribute name="name" select="$command" />
          <xsl:attribute name="desc" select="$descr" />
          <xsl:if test="$deprecated">
            <xsl:attribute name="deprecated" select="'true'" />
          </xsl:if>
          <!-- get command signature(s)... -->
          <xsl:call-template name="process-signatures">
            <xsl:with-param name="command" select="$command" tunnel="yes" />
          </xsl:call-template>
        </xsl:element>
      </xsl:otherwise>
    </xsl:choose>
    <xsl:value-of select="'&#10;'" />
  </xsl:template>

  <xsl:template name="process-signatures">
    <xsl:param name="command" tunnel="yes" />
    <xsl:variable name="lname" select="concat('|', $command, '|')" />
    <xsl:choose>
      <xsl:when test="contains($usageIn_desc_name, $lname)">
        <xsl:apply-templates select="desc/desc_signature/desc_name">
          <xsl:with-param name="command" select="$command" tunnel="yes" />
        </xsl:apply-templates>
      </xsl:when>
      <xsl:when test="contains($usageIn_section_desc_name, $lname)">
        <xsl:apply-templates select="section/desc/desc_signature/desc_name">
          <xsl:with-param name="command" select="$command" tunnel="yes" />
        </xsl:apply-templates>
      </xsl:when>
      <xsl:when test="contains($preferUsageIn_desc_name, $lname)">
        <!-- the first literal_block gives us garbage here -->
        <xsl:apply-templates
          select="section/section/desc/desc_signature/desc_name|section/desc/desc_signature/desc_name"
        >
          <xsl:with-param name="command" select="$command" tunnel="yes" />
        </xsl:apply-templates>
      </xsl:when>
      <xsl:otherwise>
        <!-- Most commands use XPATH '/document/section/literal_block[@language='cmake']' for the signature -->
        <xsl:message terminate="no" select="concat('@ literal_block: Command= ', $command)" />
        <xsl:apply-templates
          select="section[@ids='synopsis']/literal_block[@language='none']|section[not(contains(@ids, 'example'))]/literal_block[@language='cmake']|literal_block[@language='cmake' or @language='none']"
        >
          <xsl:with-param name="command" select="$command" tunnel="yes" />
        </xsl:apply-templates>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- literal_block in any section contains a single usage description -->
  <xsl:template match="literal_block[@language='cmake' or @language='none']">
    <xsl:param name="command" tunnel="yes" />
    <xsl:variable name="lcommand" select="concat('|', $command, '|')" />
    <!-- <xsl:message terminate="no" >#<xsl:value-of select="text()" />#</xsl:message> -->
    <xsl:variable name="text" select="normalize-space(string-join(node()))" />
    <!--
      <xsl:message terminate="no" select="concat('TXT #+ +',$text,'- -#')" />
    -->
    <xsl:choose>
      <xsl:when test="contains($multiUsagesIn_element, $lcommand)">
        <!-- CMAKE_PATH may have multiple usages in a single text block -->
<!--         <xsl:message terminate="no" select="concat('TXT#+#',string-join(descendant-or-self::text()),'#-#')" /> -->
        <xsl:for-each select="tokenize(string-join(descendant-or-self::text()), '&#10;')">
          <xsl:variable name="text" select="normalize-space(.)" />
<!--           <xsl:message terminate="no" select="concat('LINE #',$text,'#')" /> -->
          <xsl:if test="starts-with($text, $command)">
            <!-- remove command name from usage description -->
            <xsl:variable name="usage" select="normalize-space(substring-after($text, $command))" />
            <xsl:call-template name="filter-write-usage">
              <xsl:with-param name="command" select="$command"/>
              <xsl:with-param name="usage" select="$usage"/>
            </xsl:call-template>
          </xsl:if>
        </xsl:for-each>
<!--  <xsl:message terminate="yes" select="' DONE'" /> -->
      </xsl:when>
      <!-- skip useless texts -->
      <xsl:when test="not(starts-with($text, $command))" />
<!--       <xsl:when test="starts-with($text,'if(var')"/> -->
      <xsl:otherwise>
<!--         <xsl:variable name="text" select="normalize-space(string-join(node()))" /> -->
        <!-- remove command name from usage description -->
        <xsl:variable name="usage" select="normalize-space(substring-after($text, $command))" />
        <xsl:call-template name="filter-write-usage">
          <xsl:with-param name="command" select="$command"/>
          <xsl:with-param name="usage" select="$usage"/>
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
    <xsl:call-template name="filter-write-usage">
      <xsl:with-param name="command" select="$command"/>
      <xsl:with-param name="usage" select="$usage"/>
    </xsl:call-template>
  </xsl:template>

  <!-- write command/usage pair to plugin.xml, filter our useless usage strings -->
  <xsl:template name="filter-write-usage">
    <xsl:param name="command" />
    <xsl:param name="usage" />
    <xsl:choose>
      <!-- skip useless usages -->
      <xsl:when test="starts-with($usage,'(...')" />
      <xsl:when test="starts-with($usage,'(Experimental ')" />
      <xsl:when test="contains($usage,'myExe')" />
      <xsl:when test="contains($usage,'myexe')" />
      <xsl:when test="contains($usage,'mylib')" />
      <xsl:when test="contains($usage,'myTarget')" />
      <xsl:when test="starts-with($usage,'(foo')" />
      <xsl:when test="starts-with($usage,'(bar')" />
      <xsl:when test="$usage=''" />
      <!-- no longer needed as of cmake 3.26 <xsl:when test="contains($usage,'libfoo')"/> <xsl:when test="contains($usage,'mypro')"/> -->
      <xsl:otherwise>

        <xsl:choose>
          <xsl:when test="$process-reswords">
            <!-- extract reserved words from usage -->
            <xsl:for-each select="tokenize($usage, '[ (){}\[\]]|&lt;|&gt;|\|')">
              <!-- <xsl:message terminate="no">0RESWORD: \<xsl:value-of select="." />\</xsl:message> -->
              <xsl:choose>
                <xsl:when test="matches( ., '&#34;.+&#34;')" />
                <!-- <xsl:when test="matches( ., '&lt;.+&gt;')"/> -->
                <!-- <xsl:when test="''=." /> -->
                <xsl:when test="matches( ., '^[A-Z@][A-Z_]*[A-Z]$')">
                  <xsl:message terminate="no">RESWORD: \<xsl:value-of select="." />\</xsl:message>
                  <!-- output reserved word -->
                  <xsl:element name="word">
                    <xsl:attribute name="name" select="." />
                  </xsl:element>
                  <xsl:value-of select="'&#10;'" />
                </xsl:when>
              </xsl:choose>
            </xsl:for-each>
          </xsl:when>
          <xsl:otherwise>
            <!-- output command/usage pair to plugin.xml -->
            <xsl:message terminate="no" >USAGE: \<xsl:value-of select="$usage" />\</xsl:message>
            <xsl:value-of select="'&#10;'" />
            <xsl:element name="use">
              <xsl:attribute name="val" select="$usage" />
            </xsl:element>
            <xsl:value-of select="'&#10;'" />
          </xsl:otherwise>
        </xsl:choose>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- suppress printing of empty lines -->
  <xsl:template match="@*|node()">
    <xsl:apply-templates select="@*|node()" />
  </xsl:template>
</xsl:stylesheet>