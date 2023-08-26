function updateServerStatus(serverId, element) {
    const statusMap = new Map([
        ["UP", "Server Up"],
        ["DOWN", "Server Down"],
        ["UNKNOWN", "Unknown"]
    ]);

    $.ajax({
        url: "/api/internal/servers/get/status/" + serverId,
        method: "GET",
        success: function (status) {
            console.log("got new status (" + serverId + "): " + status)
            /*element.textContent = statusMap.get(status);
            element.classList = [];
            element.classList.add(
                status === "UP" ? "server-status-online" :
                    (status === "DOWN" ? "server-status-offline" : "server-status-unknown")
            );*/
            element.innerHTML = statusMap.get(status);
            element.classList = [];
            element.classList.add(
                status === "UP" ? "server-status-online" :
                    (status === "DOWN" ? "server-status-offline" : "server-status-unknown")
            );
        },
        error: function (error) {
            console.error("Error updating server status: " + error);
        }
    });
}

document.querySelectorAll("div#server-status").forEach(element => {
    console.log(element);
    const serverIdToUpdate = element.getAttribute("data-server-update-id");

    setInterval(function () {
        updateServerStatus(serverIdToUpdate, element);
    }, 10000);
});