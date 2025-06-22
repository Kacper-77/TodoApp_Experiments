export const refreshAccessToken = async (): Promise<string | null> => {
    const refreshToken = localStorage.getItem("refreshToken");
  
    if (!refreshToken) return null;
  
    try {
      const response = await fetch(`${import.meta.env.VITE_API_URL}/auth/refresh-token`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ refreshToken }),
      });
  
      if (!response.ok) throw new Error("Refresh token failed");
  
      const data = await response.json();
      localStorage.setItem("token", data.token);
      return data.token;
    } catch (err) {
      console.error("Failed to refresh token:", err);
      return null;
    }
  };
  