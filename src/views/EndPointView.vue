<template>
    <EndPoint />
</template>

<script>
// @ is an alias to /src
import EndPoint from "@/components/endpointOutput.vue";

export default {
  name: "EndPointView",
  components: {
    EndPoint,
  },
};
</script>

<style>
.endPoint {
  text-align: center;
  /* margin: 10px; */
}

</style>
