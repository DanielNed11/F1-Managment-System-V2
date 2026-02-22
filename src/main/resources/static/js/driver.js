async function getAllDrivers() {
    try {
        const res = await fetch('http://localhost:8080/api/drivers',
            {
                method: "GET",
                headers: {
                    Accept: "application/json"
                }
            });

        if (!res.ok) {
            throw new Error('Something went wrong')
        }
        return await res.json()
    } catch (err) {
        console.error(err)
    }
}

async function getDriversRaces(id) {
    try {
        const res = await fetch(`http://localhost:8080/api/drivers/${id}/races`,
            {
                method: "GET",
                headers: {
                    Accept: "application/json"
                }
            });

        if (!res.ok) {
            throw new Error('Something went wrong')
        }
        const races = await res.json();
        displayRacesTable(await races)

    } catch (err) {
        console.error(err)
    }
}

async function deleteDriver(id) {
    if (!confirm("Are you sure you want to delete this driver?")) return

    try {
        const res = await fetch(`http://localhost:8080/api/drivers/${id}`,
            {
                method: "DELETE",
            }
        );

        if (res.status === 204) {
            alert('Driver deleted successfully!');
            window.location.reload();
        } else if (res.status === 404) {
            alert('Driver not found');
        } else {
            alert('Failed to delete driver');
        }
    } catch (err) {
        console.error(err)
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const btn = document.getElementById("loadRacesBtn");
    if (!btn) return;

    btn.addEventListener("click", async () => {
        const driverId = btn.dataset.id;

        await getDriversRaces(driverId);
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const btn = document.getElementById("deleteDriver");
    if (!btn) return;

    btn.addEventListener("click", async () => {
        const driverId = btn.dataset.id;

        await deleteDriver(driverId);
    });
});

function displayRacesTable(races) {
    const container = document.getElementById("racesContainer");

    races.length === 0 ?
        container.innerHTML = `<p>This driver has not participated in any aces</p>` :

        container.innerHTML = `
          <table class="table table-striped table-hover align-middle">
               <thead class="table-light">
                  <tr>
                      <th>ID</th>
                      <th>Name</th>
                      <th>Date</th>
                      <th>Track</th>
                      <th>Winner</th>
                  </tr>
              </thead>
              <tbody>
                  ${races.map(race => `
                      <tr>
                          <td>${race.id}</td>
                          <td>${race.name}</td>
                          <td>${race.date}</td>
                          <td>${race.trackName || ''}</td>
                          <td>${race.winnerName || ''}</td>
                      </tr>
                  `).join('')}
              </tbody>
          </table>
      `;
}