Resource          resource.robot

*** Settings ***
Suite Setup    Open Browser    https://www.katalon.com/    chrome
Suite Teardown    Close Browser
Resource    seleniumLibrary.robot

*** Variables ***
${undefined}    https://www.katalon.com/

*** Test Cases ***
Test Case
    open    http://app.ispacebar.com/login/login.html
    type    id=userName    admin
    click    id=password
    type    id=password    password
    click    id=loginBtn
    assertTextPresent    KBTG
