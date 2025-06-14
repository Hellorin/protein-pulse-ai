<template>
  <div class="container">
    <div class="landing-content">
      <img src="@/assets/img/proteinpulseai.png" alt="Protein Pulse AI Logo" class="landing-logo">
      <router-link to="/chat" class="start-button">Start a discussion</router-link>
      
      <div class="template-section">
        <h2>Or use a template</h2>
        <div class="template-boxes">
          <router-link 
            :to="{ path: '/chat', query: { question: 'How much protein in 100 gr of silky tofu?' }}" 
            class="template-box"
          >
            How much protein in 100 gr of silky tofu?
          </router-link>
          <router-link 
            :to="{ path: '/chat', query: { question: 'I ate 100 gr of sweet potato and 100 gr of hard-boiled egg?' }}" 
            class="template-box"
          >
            I ate 100 gr of sweet potato and 100 gr of hard-boiled egg?
          </router-link>
          <router-link 
            :to="{ path: '/chat', query: { question: 'I had 40gr of pure protein and an egg?' }}" 
            class="template-box"
          >
            I had 40gr of pure protein and an egg?
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 600px;
  margin: 0 auto;
  padding: 2rem;
  background-color: #FCFCF8;
  min-height: 100vh;
}

.landing-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2rem;
}

.landing-logo {
  height: 600px;
  width: auto;
  object-fit: contain;
}

.start-button {
  display: inline-block;
  padding: 1rem 2rem;
  background-color: #4CAF50;
  color: white;
  text-decoration: none;
  border-radius: 5px;
  font-size: 1.2rem;
  transition: background-color 0.3s;
}

.start-button:hover {
  background-color: #45a049;
}

.template-section {
  width: 100%;
  max-width: 800px;
}

.template-boxes {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
  flex-wrap: nowrap;
  justify-content: space-between;
}

.template-box {
  padding: 1rem;
  background-color: #f5f5f5;
  border-radius: 5px;
  text-decoration: none;
  color: #333;
  transition: background-color 0.3s;
  flex: 1;
  text-align: center;
  min-width: 0;
}

.template-box:hover {
  background-color: #e0e0e0;
}
</style> 