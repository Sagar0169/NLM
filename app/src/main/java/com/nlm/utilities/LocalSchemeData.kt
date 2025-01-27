package com.nlm.utilities

import com.nlm.model.Form
import com.nlm.model.Scheme

object LocalSchemeData {
    val localSchemes = listOf(
        // Existing schemes
        Scheme(
            id = 198,
            name = "Livestock Health Disease",
            forms = listOf(
                Form("index", "VaccinationProgramme", 206, "Vaccination Programme", 198),
                Form("index", "MobileVeterinaryUnit", 207, "Mobile Veterinary Units", 198),
                Form("index", "Ascad", 208, "ASCAD", 198)
            )
        ),
        Scheme(
            id = 199,
            name = "National Livestock Mission (NLM)",
            forms = listOf(
                Form("index", "ImplementingAgency", 203, "Implementing Agency (Format1)", 199),
                Form("index", "RspLaboratorySemen", 221, "Regional Semen Production Laboratory(Format2)", 199),
                Form("index", "StateSemenBank", 222, "State Semen Bank (Format3)", 199),
                Form("index", "ArtificialInsemination", 223, "Artificial Insemination (Format4)", 199),
                Form("index", "ImportOfExoticGoat", 224, "Import Of Exotic Goat (Format5)", 199),
                Form("index", "AssistanceForQfsp", 225, "Assistance For Quality Fodder Seed Production(Format6)", 199),
                Form("index", "FspPlantStorage", 226, "Fodder Seed Processing Plant Storage (Format7)", 199),
                Form("index", "FpfromNonForest", 227, "Fodder Production  From Non Forest (Format8)", 199),
                Form("index", "FpFromForestLand", 228, "Fodder Production From Forest Land (Format9)", 199),
                Form("index", "AssistanceForEa", 229, "Assistance For Extension Activities(Format10)", 199),
                Form("index", "NlmEdp", 230, "NLM Entrepreneur Development Programme(Format11)", 199),
                Form("index", "Ahidf", 418, "Animal Husbandry Infrastructure Development Fund(Format12)", 199),
            )
        ),
        // New schemes and forms
        Scheme(
            id = 1,
            name = "Users",
            forms = listOf(
                Form("index", "users", 8, "All Users", 1),
                Form("index", "roles", 4, "Roles", 1),
                Form("index", "rolespermissions", 7, "Assign Role", 1),
                Form("user-log", "users", 85, "User Logs", 1),
                Form("change-password-history", "users", 91, "Password History", 1)
            )
        ),
        Scheme(
            id = 3,
            name = "Masters",
            forms = listOf(
                Form("index", "modules", 5, "Modules", 3),
                Form("index", "manage_visits", 408, "Manage Visits", 3),
                Form("index", "ImplementingAgencyMaster", 242, "Implementing Agency", 3)
            )
        ),
        Scheme(id = 201,
            name = "National Dairy Development",
            forms = listOf(
                Form("list-component-a", "NationalLevelMonitors", 219, "National Level Monitors (Component A)", 201),
                Form("reports-component-a", "NationalLevelMonitors", 234, "Reports of National Level Monitors (Component A)", 201),
                Form("list-component-b", "NationalLevelMonitors", 220, "National Level Monitors (Component B)", 201),
                Form("index", "MilkUnionVisitReport", 209, "Milk Union Visit Report", 201),
                Form("index", "DairyPlantVisitReport", 205, "Dairy Plant Visit Report", 201),
                Form("index", "DcsBmcCenterVisitReport", 210, "DCS/BMC Center Visit Report", 201),
                Form("index", "StateCenterLabVisitReport", 211, "State Center Lab Visit Report", 201),
                Form("index", "MilkProcessing", 212, "Milk Processing", 201),
                Form("index", "MilkProductMarketing", 213, "Milk Product Marketing", 201),
                Form("index", "ProductivityEnhancementServices", 214, "Productivity Enhancement Services", 201)
            )
        ),
        Scheme(id = 204,
            name = "Rashtriya Gokul Mission",
            forms = listOf(
                Form("index", "RgmImplementingAgency", 202, "State Implementing Agency", 204),
                Form("index", "RgmAiCenter", 236, "Artificial Insemination Center", 204),
                Form("index", "RgmSemenStation", 237, "Semen Stations", 204),
                Form("index", "RgmTrainingCenters", 238, "Training Centers", 204),
                Form("index", "RgmBullMotherFarms", 239, "Bull Mother Farms", 204),
                Form("index", "RgmBreedMultiplication", 240, "Breed Multiplication", 204),
                Form("index", "RgmVitroFertilization", 241, "Vitro Fertilization", 204)
            )
        )
    )
}
