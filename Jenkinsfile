@Library('opt-shared-libraries') _

//Utilisation d'une closure pour résoudre warUrl au runtime
def towerValue = { warUrl -> """{
                              "extra_vars": {
                                "self_war_url": "${warUrl}",
                                "self_environment":  "",
                                "self_drop_indices": "Non",
                                "self_drop_database": "Non"
                              }
                            }""" }
optJhipsterBuild ANSIBLE_ID: "", towerVars : towerValue


